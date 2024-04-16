package com.bltech.moxtel.features.ui.details

import app.cash.turbine.test
import com.bltech.moxtel.ViewModelCoroutineDispatcherRule
import com.bltech.moxtel.features.data.repository.FakeMovieRepository
import com.bltech.moxtel.features.domain.model.Movie
import com.bltech.moxtel.features.domain.usecase.FetchSimilarMoviesUseCase
import com.bltech.moxtel.features.domain.usecase.FetchTheMovieUseCase
import com.bltech.moxtel.features.ui.details.state.DetailsUIState
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class DetailsScreenViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @JvmField
    @OptIn(ExperimentalCoroutinesApi::class)
    @Rule
    val changeViewModelScopeRule = ViewModelCoroutineDispatcherRule(testDispatcher)

    private val dummyMovie = Movie(1435, "A", "", "")

    private val fakeMovieRepository = FakeMovieRepository()
    private val viewModel = DetailsScreenViewModel(
        FetchTheMovieUseCase(fakeMovieRepository, testDispatcher),
        FetchSimilarMoviesUseCase(fakeMovieRepository, testDispatcher),
        testDispatcher
    )

    @Test
    fun `When you start the View it should show loading`() = runTest {
        viewModel.movieFlow.test {
            assertEquals(DetailsUIState.Loading, awaitItem())
        }
    }

    @Test
    fun `When you have the movie in the DB it should show Success `() = runTest {
        fakeMovieRepository.setNextResultSetOfMovies(listOf(dummyMovie), emptyList())
        viewModel.movieFlow.test {
            assertEquals(DetailsUIState.Loading, awaitItem())
            viewModel.fetchMovie(1435)
            val result = awaitItem()
            assert(result is DetailsUIState.Success)
            assertEquals(
                (result as DetailsUIState.Success).movie,
                dummyMovie
            )
        }
    }

    @Test
    fun `When you don't have the movie in the DB it should show Error `() = runTest {
        fakeMovieRepository.setNextResultSetOfMovies(listOf(dummyMovie), emptyList())
        viewModel.movieFlow.test {
            assertEquals(DetailsUIState.Loading, awaitItem())
            viewModel.fetchMovie(1437)
            val result = awaitItem()
            assert(result is DetailsUIState.Error)
        }
    }

    @Test
    fun `When fetch movie throws exception it should show Error `() = runTest {
        fakeMovieRepository.setNextResultError(IndexOutOfBoundsException())
        viewModel.movieFlow.test {
            assertEquals(DetailsUIState.Loading, awaitItem())
            viewModel.fetchMovie(1437)
            val result = awaitItem()
            assert(result is DetailsUIState.Error)
        }
    }
}
