package com.leehendryp.codechallenge.features.list.presentation

import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.leehendryp.codechallenge.core.domain.CodeChallengeException.ClientException
import com.leehendryp.codechallenge.core.domain.CodeChallengeException.ServerException
import com.leehendryp.codechallenge.core.utils.MainCoroutineRule
import com.leehendryp.codechallenge.core.utils.NetworkChecker
import com.leehendryp.codechallenge.features.common.domain.AlbumRepository
import com.leehendryp.codechallenge.features.common.domain.MockDomainModels
import com.leehendryp.codechallenge.features.list.presentation.UIState.Snackbar
import com.leehendryp.codechallenge.features.list.presentation.UIState.Status
import com.leehendryp.codechallenge.features.utils.AlbumDiffCallback
import com.leehendryp.codechallenge.features.utils.ListUpdateCallback
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumListPresenterTest {

    @get:Rule
    internal val coroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var networkChecker: NetworkChecker

    @MockK
    private lateinit var repository: AlbumRepository

    private lateinit var presenter: AlbumListPresenter

    @Before
    fun `set up`() {
        MockKAnnotations.init(this, relaxed = true)
    }

    @Test
    fun `when presenter is initialized, should start with InitialState`() = runTest {
        every { networkChecker.isConnected() } returns flowOf()
        presenter = AlbumListPresenter(networkChecker, repository)
        advanceUntilIdle()

        assertThat(getUIState(), equalTo(UIState.InitialState))
    }

    @Test
    fun `when presenter is initialized, should start observing network checker`() = runTest {
        every { networkChecker.isConnected() } returns flowOf()
        presenter = AlbumListPresenter(networkChecker, repository)
        advanceUntilIdle()

        verify(exactly = 1) { networkChecker.isConnected() }
    }

    @Test
    fun `when network is lost, should update UI state properly`() = runTest {
        every { networkChecker.isConnected() } returns flowOf(false)
        presenter = AlbumListPresenter(networkChecker, repository)
        advanceUntilIdle()

        val expected = UIState.InitialState.copy(hasConnection = false)

        assertThat(getUIState(), equalTo(expected))
    }

    @Test
    fun `when network is connected, should update UI state properly`() = runTest {
        every { networkChecker.isConnected() } returns flow {
            emit(false) // Lee: To make sure it is not comparing at the end with InitialState
            emit(true)
        }
        presenter = AlbumListPresenter(networkChecker, repository)
        advanceUntilIdle()

        val expected = UIState.InitialState.copy(hasConnection = true)

        assertThat(getUIState(), equalTo(expected))
    }

    @Test
    fun `when network change throws generic error, should update UI state properly`() = runTest {
        every { networkChecker.isConnected() } returns flow { throw Exception() }
        presenter = AlbumListPresenter(networkChecker, repository)
        advanceUntilIdle()

        val expected = UIState.InitialState.copy(snackbar = Snackbar.Error)

        assertThat(getUIState(), equalTo(expected))
    }

    @Test
    fun `when network throws ClientException, should update UI state properly`() = runTest {
        every { networkChecker.isConnected() } returns flow { throw ClientException("") }
        presenter = AlbumListPresenter(networkChecker, repository)
        advanceUntilIdle()
        assertThat(getUIState(), equalTo(UIState.InitialState.copy(status = Status.ClientRetry)))
    }

    @Test
    fun `when network throws ServerException, should update UI state properly`() = runTest {
        every { networkChecker.isConnected() } returns flow { throw ServerException("") }
        presenter = AlbumListPresenter(networkChecker, repository)
        advanceUntilIdle()
        assertThat(getUIState(), equalTo(UIState.InitialState.copy(status = Status.ServerRetry)))
    }

    /*
    Lee May 14, 2025: Everything involving Paging3 API is excessively hard to test via unit tests.
    Part of the difficulty resides in it being an API meant to provide data via a cold asynchronous
    stream.
     */
    @Test
    fun `when intent is GetAlbums, should properly update UI state`() = runTest {
        val pagingDataFlow = flowOf(PagingData.from(MockDomainModels.mockAlbums))
        every { repository.getAlbums() } returns pagingDataFlow
        presenter = AlbumListPresenter(networkChecker, repository)
        advanceUntilIdle()

        val expected = listOf(
            UIState.InitialState,
            UIState.InitialState.copy(status = Status.Content(pagingDataFlow)),
        )

        presenter.dispatch(Intent.GetAlbums)
        val results = presenter.uiState.take(expected.size).toList()

        assertThat(results[0], equalTo(expected[0]))
        assertTrue(results[1].status is Status.Content)

        var expectedData: Any? = null
        var resultData: Any? = null

        val expectedDiffer = AsyncPagingDataDiffer(AlbumDiffCallback, ListUpdateCallback).apply {
            addOnPagesUpdatedListener {
                expectedData = snapshot().items
            }
        }

        val resultDiffer = AsyncPagingDataDiffer(AlbumDiffCallback, ListUpdateCallback).apply {
            addOnPagesUpdatedListener {
                resultData = snapshot().items
            }
        }

        val jobForExpected = launch {
            pagingDataFlow.collect { pagingData ->
                expectedDiffer.submitData(pagingData)
                advanceUntilIdle()
            }
        }

        val jobForResult = launch {
            (results[1].status as Status.Content).pagingDataFlow.collect { pagingData ->
                resultDiffer.submitData(pagingData)
                advanceUntilIdle()
            }
        }

        advanceUntilIdle()

        assertTrue(expectedData != null)
        assertTrue(resultData != null)
        assertThat(expectedData, equalTo(resultData))
        jobForExpected.cancel()
        jobForResult.cancel()
    }

    @Test
    fun `when intent is NetworkChanged, should update UI state properly`() = runTest {
        presenter = AlbumListPresenter(networkChecker, repository)
        presenter.dispatch(Intent.NetworkChanged(false))
        advanceUntilIdle()

        assertThat(getUIState(), equalTo(UIState.InitialState.copy(hasConnection = false)))

        presenter.dispatch(Intent.NetworkChanged(true))
        advanceUntilIdle()

        assertThat(getUIState(), equalTo(UIState.InitialState.copy(hasConnection = true)))
    }

    @Test
    fun `when intent is HandleError, should update UI state properly`() = runTest {
        presenter = AlbumListPresenter(networkChecker, repository)
        presenter.dispatch(Intent.HandleError(UnknownError("")))
        advanceUntilIdle()

        assertThat(getUIState(), equalTo(UIState.InitialState.copy(snackbar = Snackbar.Error)))

        presenter = AlbumListPresenter(networkChecker, repository)
        presenter.dispatch(Intent.HandleError(ServerException("")))
        advanceUntilIdle()

        assertThat(getUIState(), equalTo(UIState.InitialState.copy(status = Status.ServerRetry)))

        presenter = AlbumListPresenter(networkChecker, repository)
        presenter.dispatch(Intent.HandleError(ClientException("")))
        advanceUntilIdle()

        assertThat(getUIState(), equalTo(UIState.InitialState.copy(status = Status.ClientRetry)))
    }

    private fun getUIState() = presenter.uiState.value
}
