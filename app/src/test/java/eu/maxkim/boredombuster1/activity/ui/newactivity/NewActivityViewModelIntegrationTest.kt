package eu.maxkim.boredombuster1.activity.ui.newactivity

import eu.maxkim.boredombuster1.activity.framework.api.ActivityApiClient
import eu.maxkim.boredombuster1.activity.framework.datasource.ActivityLocalDataSourceImpl
import eu.maxkim.boredombuster1.activity.framework.datasource.ActivityRemoteDataSourceImpl
import eu.maxkim.boredombuster1.activity.framework.db.ActivityDao
import eu.maxkim.boredombuster1.activity.repository.ActivityRepositoryImpl
import eu.maxkim.boredombuster1.activity.usecase.DeleteActivityImpl
import eu.maxkim.boredombuster1.activity.usecase.GetRandomActivityImpl
import eu.maxkim.boredombuster1.activity.usecase.IsActivitySavedImpl
import eu.maxkim.boredombuster1.activity.usecase.SaveActivityImpl
import eu.maxkim.boredombuster1.util.CoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class NewActivityViewModelIntegrationTest {

    // create all the dependencies as properties of our test class: our own TestScope and
    // TestDispatcher to pass into the ApplicationRepository since we are not inside a runTest block
    // anymore
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = StandardTestDispatcher(testScheduler)
    private val testScope = TestScope(testDispatcher)

    // we have to pass our newly created testDispatcher to the CoroutineRule. Otherwise, we will
    // have multiple schedulers, and the tests will fail
    @get:Rule
    val coroutineRule = CoroutineRule(testDispatcher)

    // mock the ActivityApiClient since we already have dedicated tests with MockWebServer for that
    private val mockApiClient: ActivityApiClient = mock()
    private val mockActivityDao: ActivityDao = mock()

    // instances of real implementations all the way from the presentation layer to the framework layer
    private val remoteDataSource = ActivityRemoteDataSourceImpl(mockApiClient)
    private val localDataSource = ActivityLocalDataSourceImpl(mockActivityDao)

    private val activityRepository = ActivityRepositoryImpl(
        appScope = testScope,
        ioDispatcher = testDispatcher,
        remoteDataSource = remoteDataSource,
        localDataSource = localDataSource
    )

    private val getRandomActivity = GetRandomActivityImpl(activityRepository)
    private val saveActivity = SaveActivityImpl(activityRepository)
    private val deleteActivity = DeleteActivityImpl(activityRepository)
    private val isActivitySaved = IsActivitySavedImpl(activityRepository)

    // test that when the loadNewActivity() method is called, the ActivityApiClient appropriately
    // calls its getActivity() method
    @Test
    fun `calling loadNewActivity() triggers the api client`() = runTest {
        // Arrange
        val viewModel = NewActivityViewModel(
            getRandomActivity,
            saveActivity,
            deleteActivity,
            isActivitySaved
        )

        // Act
        viewModel.loadNewActivity()
        runCurrent()

        // Assert
        verify(mockApiClient, times(1)).getActivity()
    }
}
