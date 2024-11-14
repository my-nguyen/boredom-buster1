package eu.maxkim.boredombuster1.activity.ui.newactivity

import app.cash.turbine.test
import eu.maxkim.boredombuster1.activity.fake.usecase.FakeDeleteActivity
import eu.maxkim.boredombuster1.activity.fake.usecase.FakeGetRandomActivity
import eu.maxkim.boredombuster1.activity.fake.usecase.FakeIsActivitySaved
import eu.maxkim.boredombuster1.activity.fake.usecase.FakeSaveActivity
import eu.maxkim.boredombuster1.activity.fake.usecase.activity1
import eu.maxkim.boredombuster1.activity.fake.usecase.activity2
import eu.maxkim.boredombuster1.util.CoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
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

    // test whether the new activity loads and replaces the current one when calling loadNewActivity() explicitly
    @Test
    fun `calling loadNewActivity() updates ui state with a new activity`() {
        // Arrange
        val fakeGetRandomActivity = FakeGetRandomActivity()
        val viewModel = NewActivityViewModel(
            fakeGetRandomActivity,
            FakeSaveActivity(),
            FakeDeleteActivity(),
            FakeIsActivitySaved()
        )
        // this can be omitted, but it is nice to not have any pending tasks
        coroutineRule.testDispatcher.scheduler.runCurrent()

        val expectedUiState = NewActivityUiState.Success(activity2, false)
        fakeGetRandomActivity.activity = activity2

        // Act
        viewModel.loadNewActivity()
        coroutineRule.testDispatcher.scheduler.runCurrent()

        // Assert
        val actualState = viewModel.uiState.value
        assertEquals(actualState, expectedUiState)
    }

    // test whether or not calling setIsFavorite(activity, true) method interacts with the correct use case
    @Test
    fun `calling setIsFavorite(true) triggers SaveActivity use case`() {
        // Arrange
        val fakeSaveActivity = FakeSaveActivity()
        val viewModel = NewActivityViewModel(
            FakeGetRandomActivity(),
            fakeSaveActivity,
            FakeDeleteActivity(),
            FakeIsActivitySaved()
        )

        // Act
        viewModel.setIsFavorite(activity1, true)
        coroutineRule.testDispatcher.scheduler.runCurrent()

        // Assert
        assert(fakeSaveActivity.wasCalled)
    }

    @Test
    fun `calling setIsFavorite(false) triggers DeleteActivity use case`() {
        // Arrange
        val fakeDeleteActivity = FakeDeleteActivity()
        val viewModel = NewActivityViewModel(
            FakeGetRandomActivity(),
            FakeSaveActivity(),
            fakeDeleteActivity,
            FakeIsActivitySaved()
        )

        // Act
        viewModel.setIsFavorite(activity1, false)
        coroutineRule.testDispatcher.scheduler.runCurrent()

        // Assert
        assert(fakeDeleteActivity.wasCalled)
    }

    // Turbine is a small library that makes testing Flows really easy and intuitive
    // we can test our Flows in the following manner:
    // flowOf("one", "two").test {
    //     assertEquals("one", awaitItem())
    //     assertEquals("two", awaitItem())
    //     awaitComplete()
    // }
    // The Turbine's test() function is a suspend function, so we will need to run our test with the
    // runTest coroutine builder
    @Test
    fun `calling loadNewActivity() twice goes through expected ui states`() = runTest {
        val fakeGetRandomActivity = FakeGetRandomActivity()
        val viewModel = NewActivityViewModel(
            fakeGetRandomActivity,
            FakeSaveActivity(),
            FakeDeleteActivity(),
            FakeIsActivitySaved()
        )

        // We want the initial state of UI to be Loading
        assert(viewModel.uiState.value is NewActivityUiState.Loading)

        // have to collect our flow with the test() function in a separate coroutine so it is not
        // suspending the whole test
        launch {
            viewModel.uiState.test {
                with(awaitItem()) {
                    // after the first load completes, we expect the state to change to Success with
                    // the corresponding activity
                    assert(this is NewActivityUiState.Success)
                    assertEquals((this as NewActivityUiState.Success).activity, activity1)
                }

                // Calling loadNewActivity() again should change the state back to Loading
                assert(awaitItem() is NewActivityUiState.Loading)

                with(awaitItem()) {
                    // finish with another Success containing the second activity
                    assert(this is NewActivityUiState.Success)
                    assertEquals((this as NewActivityUiState.Success).activity, activity2)
                }
                // this is not necessary for this specific test but better safe than sorry
                // especially when dealing with hot flows
                cancelAndIgnoreRemainingEvents()
            }
        }

        // runs the initial loading
        runCurrent()

        // prepares and runs the second loading
        fakeGetRandomActivity.activity = activity2
        viewModel.loadNewActivity()
        runCurrent()
    }

}