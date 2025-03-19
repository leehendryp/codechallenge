package com.leehendryp.codechallenge.features.feed

import com.leehendryp.codechallenge.core.presentation.BasePresenter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

internal sealed interface Intent

internal data class UIState(
    val status: Status,
) {
    sealed interface Status {
        data object Loading : Status
    }

    companion object {
        val InitialState = UIState(Status.Loading)
    }
}

internal sealed interface UISideEffect

@HiltViewModel
internal class AlbumFeedPresenter @Inject constructor() : BasePresenter<Intent, UIState, UISideEffect>() {

    override fun setInitialState(): UIState = UIState.InitialState

    override fun process(intent: Intent) {}
}
