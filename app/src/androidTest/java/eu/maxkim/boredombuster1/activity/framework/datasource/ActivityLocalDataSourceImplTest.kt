package eu.maxkim.boredombuster1.activity.framework.datasource

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import eu.maxkim.boredombuster1.activity.framework.db.ActivityDao
import eu.maxkim.boredombuster1.activity.model.Activity
import eu.maxkim.boredombuster1.framework.AppDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.io.IOException

// since we need a Context to create a database, we will be creating our new test class in the
// 'androidTest' folder instead of the 'test' one
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ActivityLocalDataSourceImplTest {
    private lateinit var activityDao: ActivityDao
    private lateinit var database: AppDatabase

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val activityListObserver: Observer<List<Activity>> = mock()

    @Captor
    private lateinit var activityListCaptor: ArgumentCaptor<List<Activity>>

    // We won't be reusing this logic outside this test class, so there is no need to create a
    // separate Rule
    @Before
    fun createDb() {
        // To create an instance of a Room database, we will need an application Context
        val context = ApplicationProvider.getApplicationContext<Context>()
        // create an instance of a Room database. but we don't want to create a real database every
        // time we run a test - that would be a bit of an overkill. Luckily, Room provides us with
        // inMemoryDatabaseBuilder API, which builds an in-memory version of the database
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        activityDao = database.activityDao()
    }


    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    // checks if we can save an activity to the database and access it.
    // We will need to use runTest since we will be using suspend functions, but we don't need our
    // CoroutineRule since we are not in the UI layer anymore and won't be using Dispatchers.Main.
    // The TestScope provided by runTest will be enough.
    @Test
    fun canSaveActivityToTheDbAndReadIt() = runTest {
        // Arrange
        val activityLocalDataSource = ActivityLocalDataSourceImpl(activityDao)

        // Act
        activityLocalDataSource.saveActivity(androidActivity1)

        // Assert
        assert(activityLocalDataSource.isActivitySaved(androidActivity1.key))
    }

    // it's about deleting an activity from the database, very similar test to the previous one
    @Test
    fun canDeleteActivityFromTheDb() = runTest {
        // Arrange
        val activityLocalDataSource = ActivityLocalDataSourceImpl(activityDao)
        activityLocalDataSource.saveActivity(androidActivity1)

        // Act
        activityLocalDataSource.deleteActivity(androidActivity1)

        // Assert
        assert(!activityLocalDataSource.isActivitySaved(androidActivity1.key))
    }

    // test the LiveData coming straight from the database
    @Test
    fun canSaveActivityToTheDbAndObserveTheLiveData() = runTest {
        // Arrange
        val activityLocalDataSource = ActivityLocalDataSourceImpl(activityDao)
        val expectedList = listOf(androidActivity1, androidActivity2)

        // Act
        // start observing the database when there are no entries
        activityLocalDataSource.getActivityListLiveData().observeForever(activityListObserver)
        activityLocalDataSource.saveActivity(androidActivity1)
        activityLocalDataSource.saveActivity(androidActivity2)

        // Assert
        // our first onChanged event will fire with an empty list. We expect our LiveData to be
        // updated after every save, hence onChanged should be triggered three times.
        verify(activityListObserver, times(3)).onChanged(activityListCaptor.capture())
        // the final list should contain both activities.
        assertEquals(activityListCaptor.value, expectedList)
    }
}
