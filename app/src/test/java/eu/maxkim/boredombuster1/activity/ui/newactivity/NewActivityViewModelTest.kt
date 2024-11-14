package eu.maxkim.boredombuster1.activity.ui.newactivity

import eu.maxkim.boredombuster1.activity.fake.usecase.FakeDeleteActivity
import eu.maxkim.boredombuster1.activity.fake.usecase.FakeGetRandomActivity
import eu.maxkim.boredombuster1.activity.fake.usecase.FakeIsActivitySaved
import eu.maxkim.boredombuster1.activity.fake.usecase.FakeSaveActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class NewActivityViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

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

}