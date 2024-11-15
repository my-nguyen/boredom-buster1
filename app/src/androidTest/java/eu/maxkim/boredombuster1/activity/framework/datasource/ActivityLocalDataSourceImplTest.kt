package eu.maxkim.boredombuster1.activity.framework.datasource

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import eu.maxkim.boredombuster1.activity.framework.db.ActivityDao
import eu.maxkim.boredombuster1.framework.AppDatabase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

// since we need a Context to create a database, we will be creating our new test class in the
// 'androidTest' folder instead of the 'test' one
class ActivityLocalDataSourceImplTest {
    private lateinit var activityDao: ActivityDao
    private lateinit var database: AppDatabase

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

}
