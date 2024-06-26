package com.bltech.moxtel.features.ui.home

import app.cash.turbine.test
import com.bltech.moxtel.ViewModelCoroutineDispatcherRule
import com.bltech.moxtel.features.data.repository.FakeMovieRepository
import com.bltech.moxtel.features.domain.model.Movie
import com.bltech.moxtel.features.domain.usecase.FetchMoviesUseCase
import com.bltech.moxtel.features.domain.usecase.IFetchMoviesUseCase
import com.bltech.moxtel.features.ui.home.state.GalleryUIState
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()


    @JvmField
    @OptIn(ExperimentalCoroutinesApi::class)
    @Rule
    val changeViewModelScopeRule = ViewModelCoroutineDispatcherRule(testDispatcher)

    private val mockUseCase = mockk<IFetchMoviesUseCase>()

    private val dummyMovie = Movie(1435, "A", "", "")

    private val fakeMovieRepository = FakeMovieRepository()


    @Test
    fun `when you start it should be first event should be loading`() = runTest {
        val viewModel = HomeViewModel(mockUseCase, testDispatcher)
        every { mockUseCase.getMoviesFlow() } returns flowOf(listOf(dummyMovie))
        viewModel.moviesFlow.test {
            assertEquals(GalleryUIState.Loading, awaitItem())
            awaitItem()
        }
    }

    @Test
    fun `If there are no movies in db, then it should only show loading`() = runTest {
        val viewModel = HomeViewModel(mockUseCase, testDispatcher)
        every { mockUseCase.getMoviesFlow() } returns flowOf(listOf())
        viewModel.moviesFlow.test {
            assertEquals(GalleryUIState.Loading, awaitItem())
        }
    }

    @Test
    fun `If there are errors in API request, then it should show error`() = runTest {
        val viewModel = HomeViewModel(mockUseCase, testDispatcher)
        every { mockUseCase.getMoviesFlow() } returns flowOf(listOf())
        coEvery { mockUseCase.downloadMovies() } throws IndexOutOfBoundsException()
        viewModel.moviesFlow.test {
            assertEquals(GalleryUIState.Loading, awaitItem())
            viewModel.fetchMovies()
            assert(awaitItem() is GalleryUIState.Error)
        }
    }

    @Test
    fun `If there are no Errors in API request, then it should show success`() = runTest {
        val viewModel =
            HomeViewModel(FetchMoviesUseCase(fakeMovieRepository, testDispatcher), testDispatcher)
        viewModel.moviesFlow.test {
            assertEquals(GalleryUIState.Loading, awaitItem())
            fakeMovieRepository.setNextResultSetOfMovies(listOf(dummyMovie), emptyList())
            viewModel.fetchMovies()
            val result = awaitItem()
            assert(result is GalleryUIState.Success)
            assertEquals((result as GalleryUIState.Success).movies.first(), dummyMovie.toUI())
        }
    }

    @Test
    fun `If there are is Errors flow then it should show Error`() = runTest {
        val viewModel =
            HomeViewModel(FetchMoviesUseCase(fakeMovieRepository, testDispatcher), testDispatcher)
        viewModel.moviesFlow.test {
            assertEquals(GalleryUIState.Loading, awaitItem())
            fakeMovieRepository.setNextResultError(Exception("Fake Exception"))
            viewModel.fetchMovies()
            val result = awaitItem()
            assert(result is GalleryUIState.Error)
            assertEquals((result as GalleryUIState.Error).detail, "Fake Exception")
        }
    }
}
