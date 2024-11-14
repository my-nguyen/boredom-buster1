package eu.maxkim.boredombuster1.activity.ui.favorite

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import eu.maxkim.boredombuster1.activity.fake.usecase.activity1
import eu.maxkim.boredombuster1.activity.fake.usecase.activity2
import eu.maxkim.boredombuster1.activity.model.Activity
import eu.maxkim.boredombuster1.activity.usecase.DeleteActivity
import eu.maxkim.boredombuster1.activity.usecase.GetFavoriteActivities
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

// unlike StateFlow, LiveData doesn't have a mandatory initial value. Moreover, LiveData is written
// in Java, and its value doesn't always make sense in Kotlin.
// For example, our LiveData type is LiveData<FavoritesUiState>, so one would assume that the value
// is non-nullable. However, that is not the case.
// If we create a new instance of FavoritesViewModel and immediately check
// viewModel.uiStateLiveData.value it will be null.
// So to properly test our LiveData, we have to replicate the observer pattern.
// let's use mocks for this one.
// The most popular mock library out there is Mockito,
// we will need to either run our test with the MockitoJUnitRunner or add a MockitoRule
@RunWith(MockitoJUnitRunner::class)
class FavoritesViewModelTest {

    // The class that we are interested in is InstantTaskExecutorRule, which does exactly what our
    // CoroutineRule does, only for Architecture components
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // use the mock() method from mockito-kotlin library
    private val mockGetFavoriteActivities: GetFavoriteActivities = mock()
    private val mockDeleteActivity: DeleteActivity = mock()

    // an Observer mock
    private val activityListObserver: Observer<FavoritesUiState> = mock()

    // we need an Observer to observe our LiveData and something that can capture the observed data
    // and preserve it in a state for us to use in our assertions. we can use an ArgumentCaptor<T>
    // from Mockito to do this for us use the @Captor annotation to generate the captor
    @Captor
    private lateinit var activityListCaptor: ArgumentCaptor<FavoritesUiState>

    @Test
    fun `the view model maps list of activities to list ui state`() {
        // ARRANGE
        // a LiveData object that our mock will return
        val liveDataToReturn = MutableLiveData<List<Activity>>()
            .apply { value = listOf(activity1, activity2) }

        // a list we expect to observe
        // We should not feed expectedList directly into our LiveData because expected and actual
        // values will be the same reference, which can muddy the test result in some cases.
        val expectedList = listOf(activity1, activity2)

        // To set up our mock, we will use Mockito's whenever() together with Kotlin's doReturn()
        whenever(mockGetFavoriteActivities.invoke()).doReturn(liveDataToReturn)

        val viewModel = FavoritesViewModel(mockGetFavoriteActivities, mockDeleteActivity)

        // ACT
        // start observing the LiveData with our mock Observer
        // We are using observeForever since we don't have a LifecycleOwner
        viewModel.uiStateLiveData.observeForever(activityListObserver)

        // ASSERT
        // verify that our mock Observer’s onChanged method is called once, and that we are using
        // the activityListCaptor to capture the passed value
        verify(activityListObserver, times(1)).onChanged(activityListCaptor.capture())
        // access the activityListCaptor.value, which is a FavoritesUiState
        assert(activityListCaptor.value is FavoritesUiState.List)

        // compare its activityList to expectedList
        val actualList = (activityListCaptor.value as FavoritesUiState.List).activityList
        assertEquals(actualList, expectedList)
    }

    // similar to the first one, but we will be returning an empty list from our mock's LiveData and
    // asserting that the FavoritesUiState is Empty
    @Test
    fun `the view model maps empty list of activities to empty ui state`() {
        // Arrange
        val liveDataToReturn = MutableLiveData<List<Activity>>()
            .apply { value = listOf() }

        whenever(mockGetFavoriteActivities.invoke()).doReturn(liveDataToReturn)

        val viewModel = FavoritesViewModel(
            mockGetFavoriteActivities,
            mockDeleteActivity
        )

        // Act
        viewModel.uiStateLiveData.observeForever(activityListObserver)

        // Assert
        verify(activityListObserver, times(1)).onChanged(activityListCaptor.capture())
        assert(activityListCaptor.value is FavoritesUiState.Empty)
    }
}
