package com.leehendryp.codechallenge.features.list.presentation

import androidx.lifecycle.viewModelScope
import com.leehendryp.codechallenge.core.presentation.BasePresenter
import com.leehendryp.codechallenge.features.list.domain.Album
import com.leehendryp.codechallenge.features.list.domain.AlbumRepository
import com.leehendryp.codechallenge.features.list.presentation.UIState.SnackBar
import com.leehendryp.codechallenge.features.list.presentation.UIState.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

internal sealed interface Intent {
    data object FetchData : Intent
}

internal data class UIState(
    val status: Status,
    val snackBar: SnackBar? = null,
) {
    sealed interface Status {
        data object LoadingList : Status
        data object Empty : Status
        data class Content(val albums: List<Album>) : Status
    }

    sealed interface SnackBar {
        data object Error : SnackBar
    }

    companion object {
        val InitialState = UIState(Status.LoadingList)
    }
}

internal sealed interface UISideEffect

@HiltViewModel
internal class AlbumListPresenter @Inject constructor(
    private val repository: AlbumRepository,
) : BasePresenter<Intent, UIState, UISideEffect>() {

    override fun setInitialState(): UIState = UIState.InitialState

    override fun process(intent: Intent) {
        when (intent) {
            is Intent.FetchData -> fetchData()
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            repository.getAlbums()
                .onStart {
                    updateState { copy(status = Status.LoadingList, snackBar = null) }
                }
                .catch {
                    updateState { copy(status = Status.Empty, snackBar = SnackBar.Error) }
                }
                .collect { result ->
                    updateState {
                        copy(status = Status.Content(result), snackBar = null)
                    }
                }
        }
    }
}
