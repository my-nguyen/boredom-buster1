package eu.maxkim.boredombuster1.activity.ui.newactivity

import eu.maxkim.boredombuster1.activity.fake.usecase.FakeDeleteActivity
import eu.maxkim.boredombuster1.activity.fake.usecase.FakeGetRandomActivity
import eu.maxkim.boredombuster1.activity.fake.usecase.FakeIsActivitySaved
import eu.maxkim.boredombuster1.activity.fake.usecase.FakeSaveActivity
import eu.maxkim.boredombuster1.util.CoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

}