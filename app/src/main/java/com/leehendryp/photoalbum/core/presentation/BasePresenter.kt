package com.leehendryp.photoalbum.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal abstract class BasePresenter<in Intent, UIState, UISideEffect> : ViewModel() {

    private val _uiState by lazy { MutableStateFlow(setInitialState()) }
    val uiState: StateFlow<UIState> = _uiState

    private val intent: MutableSharedFlow<Intent> = MutableSharedFlow()

    private val _uiSideEffect: Channel<UISideEffect> = Channel()
    val uiSideEffect = _uiSideEffect.receiveAsFlow()

    init {
        observeIntents()
    }

    protected abstract fun setInitialState(): UIState

    protected abstract fun process(intent: Intent)

    private fun observeIntents() {
        viewModelScope.launch {
            intent.collect {
                process(it)
            }
        }
    }

    fun dispatch(vararg intent: Intent) {
        viewModelScope.launch {
            intent.forEach { this@BasePresenter.intent.emit(it) }
        }
    }

    protected fun updateState(reducer: UIState.() -> UIState) {
        _uiState.update(reducer)
    }

    protected fun sendSideEffect(builder: () -> UISideEffect) {
        viewModelScope.launch {
            _uiSideEffect.send(builder())
        }
    }
}
