package com.leehendryp.codechallenge.features.details.presentation

import androidx.lifecycle.viewModelScope
import com.leehendryp.codechallenge.core.presentation.BasePresenter
import com.leehendryp.codechallenge.features.list.domain.Album
import com.leehendryp.codechallenge.features.list.domain.AlbumRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

internal sealed interface Intent {
    data class GetAlbum(val id: Int) : Intent
}

internal sealed interface UIState {
    data object LoadingList : UIState
    data object Retry : UIState
    data class Content(val album: Album) : UIState

    companion object {
        val InitialState = LoadingList
    }
}

internal sealed interface UISideEffect // Lee: No use, for now.

@HiltViewModel
internal class AlbumDetailsPresenter @Inject constructor(
    private val repository: AlbumRepository,
) : BasePresenter<Intent, UIState, UISideEffect>() {

    override fun setInitialState(): UIState = UIState.InitialState

    override fun process(intent: Intent) {
        when (intent) {
            is Intent.GetAlbum -> getAlbums(intent.id)
        }
    }

    private fun getAlbums(id: Int) {
        viewModelScope.launch {
            repository.getAlbum(id)
                .onStart {
                    updateState { UIState.LoadingList }
                }
                .catch {
                    it
                    updateState { UIState.Retry }
                }
                .collect { album ->
                    updateState { UIState.Content(album) }
                }
        }
    }
}
