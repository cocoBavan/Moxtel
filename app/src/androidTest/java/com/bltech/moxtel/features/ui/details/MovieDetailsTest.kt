package com.bltech.moxtel.features.ui.details

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.bltech.moxtel.features.data.repository.FakeMovieRepository
import com.bltech.moxtel.features.domain.model.Movie
import com.bltech.moxtel.global.TitleSetter
import com.bltech.moxtel.global.theme.MoxtelTheme
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MovieDetailsTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val titleSetter = object : TitleSetter {
        override fun setTitle(title: String) {}
    }
    private val fakeMovieRepository = FakeMovieRepository()
    private val viewModel = DetailsScreenViewModel(fakeMovieRepository)

    private val dummyMovie = Movie(1435, "A Testable Movie", "", "")


    @Before
    fun cleanUp() {
        fakeMovieRepository.clear()
    }

    @Test
    fun `when a the page is launched it should show loading`() {
        fakeMovieRepository.setDelay(1000)
        composeTestRule.setContent {
            MoxtelTheme {
                DetailsScreen(
                    movieId = 1436,
                    navController = rememberNavController(),
                    titleSetter = titleSetter,
                    viewModel = viewModel
                )
            }
        }
        composeTestRule.onNodeWithTag("loading").assertIsDisplayed()
    }

    @Test
    fun `when a there is no movie in the response it should show error`() {
        composeTestRule.setContent {
            MoxtelTheme {
                DetailsScreen(
                    movieId = 1436,
                    navController = rememberNavController(),
                    titleSetter = titleSetter,
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.waitUntil(5000) {
            composeTestRule.onNodeWithTag("error").isDisplayed()
        }
        composeTestRule.onNodeWithTag("error").assertIsDisplayed()
    }

    @Test
    fun `when a there is movie in the response it should show details page with its title in it`() {
        fakeMovieRepository.setNextResultSetOfMovies(listOf(dummyMovie), emptyList())
        composeTestRule.setContent {
            MoxtelTheme {
                DetailsScreen(
                    movieId = 1435,
                    navController = rememberNavController(),
                    titleSetter = titleSetter,
                    viewModel = viewModel
                )
            }
        }

        composeTestRule.waitUntil(5000) {
            composeTestRule.onNodeWithTag("detail_view").isDisplayed()
        }
        composeTestRule.onNodeWithText(dummyMovie.title).assertIsDisplayed()
        /*
        TODO: Assert rest of the elements in the UI are displayed.
         */
    }
}
