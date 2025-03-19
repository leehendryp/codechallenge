package com.leehendryp.codechallenge.core.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class BasePresenterTest {

    private sealed interface Intent {
        data object SomeIntent : Intent
        data object SomeOtherIntent : Intent
        data object YetAnotherIntent : Intent
    }

    private sealed interface UIState {
        data object InitialUIState : UIState
        data object SomeUIState : UIState
        data object SomeOtherUIState : UIState
    }

    private sealed interface UISideEffect {
        data object SomeSideEffect : UISideEffect
    }

    private lateinit var presenter: BasePresenter<Intent, UIState, UISideEffect>
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun `set up`() {
        Dispatchers.setMain(testDispatcher)
        presenter = object : BasePresenter<Intent, UIState, UISideEffect>() {
            override fun setInitialState(): UIState = UIState.InitialUIState
            override fun process(intent: Intent) {
                when (intent) {
                    Intent.SomeIntent -> updateState { UIState.SomeUIState }
                    Intent.SomeOtherIntent -> updateState { UIState.SomeOtherUIState }
                    Intent.YetAnotherIntent -> sendSideEffect { UISideEffect.SomeSideEffect }
                }
            }
        }
    }

    @After
    fun `tear down`() {
        Dispatchers.resetMain()
    }

    private fun getUIState() = presenter.uiState.value

    @Test
    fun `initialization should default to result of setInitialState`() = runTest {
        val result = getUIState()
        advanceUntilIdle()

        assertThat(result, equalTo(UIState.InitialUIState))
    }

    @Test
    fun `dispatching SomeIntent should update UI state to SomeUIState`() = runTest {
        presenter.dispatch(Intent.SomeIntent)
        advanceUntilIdle()

        assertThat(getUIState(), equalTo(UIState.SomeUIState))
    }

    @Test
    fun `dispatching SomeOtherIntent should update UI state to SomeOtherUIState`() = runTest {
        presenter.dispatch(Intent.SomeOtherIntent)
        advanceUntilIdle()

        assertThat(getUIState(), equalTo(UIState.SomeOtherUIState))
    }

    @Test
    fun `dispatching side effect should not interfere with state updates`() = runTest {
        presenter.dispatch(Intent.SomeOtherIntent, Intent.YetAnotherIntent)
        advanceUntilIdle()

        assertThat(getUIState(), equalTo(UIState.SomeOtherUIState))
        assertThat(presenter.uiSideEffect.first(), equalTo(UISideEffect.SomeSideEffect))
    }

    @Test
    fun `dispatching multiple intents should result in sequential emission`() = runTest {
        presenter.dispatch(Intent.SomeIntent, Intent.SomeOtherIntent, Intent.SomeIntent)

        val expected = listOf(
            UIState.InitialUIState,
            UIState.SomeUIState,
            UIState.SomeOtherUIState,
            UIState.SomeUIState,
        )
        val result = presenter.uiState.take(expected.size).toList()

        assertThat(result, equalTo(expected))
    }

    @Test
    fun `dispatching duplicate intents should not result in duplicate states`() = runTest {
        presenter.dispatch(Intent.SomeIntent, Intent.SomeIntent, Intent.SomeOtherIntent)

        val expected = listOf(UIState.InitialUIState, UIState.SomeUIState, UIState.SomeOtherUIState)
        val result = presenter.uiState.take(expected.size).toList()

        assertThat(result, equalTo(expected))
    }
}
