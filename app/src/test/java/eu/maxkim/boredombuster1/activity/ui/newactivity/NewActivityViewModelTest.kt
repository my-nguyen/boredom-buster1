package eu.maxkim.boredombuster1.activity.ui.newactivity

import eu.maxkim.boredombuster1.activity.fake.usecase.FakeDeleteActivity
import eu.maxkim.boredombuster1.activity.fake.usecase.FakeGetRandomActivity
import eu.maxkim.boredombuster1.activity.fake.usecase.FakeIsActivitySaved
import eu.maxkim.boredombuster1.activity.fake.usecase.FakeSaveActivity
import eu.maxkim.boredombuster1.activity.fake.usecase.activity1
import eu.maxkim.boredombuster1.util.CoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class NewActivityViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    // test that after creating the viewModel the initial value of the uiState is NewActivityUiState.Loading
    @Test
    fun `creating a viewmodel exposes loading ui state`() {
        // Arrange
        val viewModel = NewActivityViewModel(
            FakeGetRandomActivity(),
            FakeSaveActivity(),
            FakeDeleteActivity(),
            FakeIsActivitySaved()
        )

        // Assert
        // StateFlow always has a value or a state (hence the name), so we can access it synchronously
        // with viewModel.uiState.value. We don't need to be inside a coroutine to do that.
        assert(viewModel.uiState.value is NewActivityUiState.Loading)
    }

    // test that after an instance of NewActivityViewModel is created and all the coroutines have
    // successfully finished, the UI state is NewActivityUiState.Success
    @Test
    fun `creating a viewmodel updates ui state to success after loading`() {
        // Arrange
        val viewModel = NewActivityViewModel(
            FakeGetRandomActivity(),
            FakeSaveActivity(),
            FakeDeleteActivity(),
            FakeIsActivitySaved()
        )

        val expectedUiState = NewActivityUiState.Success(activity1, false)

        // Act
        // execute our coroutine that loads a new activity from the FakeGetRandomActivity use case,
        // which is calling runCurrent() on a TestCoroutineScheduler used by our TestDispatcher
        coroutineRule.testDispatcher.scheduler.runCurrent()

        // Assert
        val actualState = viewModel.uiState.value
        assertEquals(actualState, expectedUiState)
    }

    // test that the UI state correctly updates to NewActivityUiState.Error if something goes wrong.
    // All we have to do is to make our fake return an error
    @Test
    fun `creating a viewmodel updates ui state to error in case of failure`() {
        // Arrange
        val viewModel = NewActivityViewModel(
            FakeGetRandomActivity(isSuccessful = false), // our fake will return an error
            FakeSaveActivity(),
            FakeDeleteActivity(),
            FakeIsActivitySaved()
        )

        // Act
        coroutineRule.testDispatcher.scheduler.runCurrent()

        // Assert
        val currentState = viewModel.uiState.value
        assert(currentState is NewActivityUiState.Error)
    }

    //  test that, if the activity is already saved, the UI state's isFavorite flag is set to true
    @Test
    fun `if activity is already saved, ui state's isFavorite is set to true`() {
        // Arrange
        val viewModel = NewActivityViewModel(
            FakeGetRandomActivity(), // our fake will return an error
            FakeSaveActivity(),
            FakeDeleteActivity(),
            FakeIsActivitySaved(isActivitySaved = true)
        )

        val expectedUiState = NewActivityUiState.Success(activity1, true)

        // Act
        coroutineRule.testDispatcher.scheduler.runCurrent()

        // Assert
        val actualState = viewModel.uiState.value
        assertEquals(actualState, expectedUiState)
    }
}