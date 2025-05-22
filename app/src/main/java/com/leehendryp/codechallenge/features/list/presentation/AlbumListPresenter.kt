package com.leehendryp.codechallenge.features.list.presentation

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.leehendryp.codechallenge.core.domain.CodeChallengeException
import com.leehendryp.codechallenge.core.presentation.BasePresenter
import com.leehendryp.codechallenge.core.utils.NetworkChecker
import com.leehendryp.codechallenge.features.common.domain.Album
import com.leehendryp.codechallenge.features.common.domain.AlbumRepository
import com.leehendryp.codechallenge.features.list.presentation.UIState.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

internal sealed interface Intent {
    data class NetworkChanged(val hasConnection: Boolean) : Intent
    data object GetAlbums : Intent
    object HandleNoItems : Intent
    data class HandleError(val error: Throwable) : Intent
    data class SeeDetails(val id: Int) : Intent
}

internal data class UIState(
    val status: Status,
    val hasConnection: Boolean,
    val snackbar: Snackbar? = null,
) {
    sealed interface Status {
        data object LoadingList : Status
        data object ClientRetry : Status
        data object ServerRetry : Status
        data object Empty : Status

        // Lee May 12, 2025: MVI is based on providing snapshots of data-bound information to the UI.
        // However, since Paging3 is too opinionated, I have to let go of this snapshot control here
        // and provide a Flow<PagingData<T>> directly to LazyColumn, in order to make better use
        // of the Paging3-Compose integration.
        data class Content(val pagingDataFlow: Flow<PagingData<Album>>) : Status
    }

    sealed interface Snackbar {
        data object Error : Snackbar
    }

    companion object {
        val InitialState = UIState(status = Status.LoadingList, hasConnection = true)
    }
}

internal sealed interface UISideEffect {
    data class GoToDetails(val id: Int) : UISideEffect
}

@HiltViewModel
internal class AlbumListPresenter @Inject constructor(
    networkChecker: NetworkChecker,
    private val repository: AlbumRepository,
) : BasePresenter<Intent, UIState, UISideEffect>() {

    init {
        networkChecker.isConnected()
            .onEach { dispatch(Intent.NetworkChanged(it)) }
            .catch { dispatch(Intent.HandleError(it)) }
            .launchIn(viewModelScope)
    }

    override fun setInitialState(): UIState = UIState.InitialState

    override fun process(intent: Intent) {
        when (intent) {
            is Intent.GetAlbums -> getAlbums()
            is Intent.HandleNoItems -> updateState { copy(status = Status.Empty) }
            is Intent.HandleError -> handleError(intent.error)
            is Intent.NetworkChanged -> updateState { copy(hasConnection = intent.hasConnection) }
            is Intent.SeeDetails -> sendSideEffect { UISideEffect.GoToDetails(intent.id) }
        }
    }

    private fun getAlbums() {
        viewModelScope.launch {
            updateState { copy(status = Status.LoadingList) }

            val pagingDataFlow = repository.getAlbums().cachedIn(viewModelScope)

            updateState { copy(status = Status.Content(pagingDataFlow)) }
        }
    }

    private fun handleError(error: Throwable) {
        when (error) {
            is CodeChallengeException.ClientException ->
                updateState { copy(status = Status.ClientRetry) }

            is CodeChallengeException.ServerException ->
                updateState { copy(status = Status.ServerRetry) }

            else -> updateState { copy(snackbar = UIState.Snackbar.Error) }
        }
    }
}
