package eu.maxkim.boredombuster1.activity.repository

import eu.maxkim.boredombuster1.activity.fake.usecase.activity1
import eu.maxkim.boredombuster1.activity.framework.datasource.FakeActivityLocalDataSource
import eu.maxkim.boredombuster1.activity.framework.datasource.FakeActivityRemoteDataSource
import eu.maxkim.boredombuster1.model.Result
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/*
Another important testing case that often comes up in Android development is when we have a
component that uses either a custom CoroutineScope or switches to a different CoroutineDispatcher.

CoroutineScopes in the presentation layer (viewModelScope and lifecycleScope) use Dispatchec.Main,
and we can easily set our own main dispatcher with Dispatchers.setMain(). It is enough for testing
coroutines that run on those scopes.

However, if we use, for example, an application-scoped CoroutineScope that runs on
Dispatchers.Default, we cannot just replace Dispatchers.Default with our own TestDispatcher.

The same applies to switching to a different CoroutineDispatcher with the withContext() function.
For example, if we want to do some network calls on Dispatchers.IO or heavy calculations on
Dispatchers.Default.

When testing scenarios with multiple scopes and dispatchers, the most important thing to keep in
mind is that during a test all those scopes and dispatchers must share the same instance of a
TestCoroutineScheduler.

val testScheduler: TestCoroutineScheduler = TestCoroutineScheduler()
val testDispatcher: TestDispatcher = StandardTestDispatcher(testScheduler)
val testScope: TestScope = TestScope(testDispatcher)

The main takeaway here is that you can pass a TestCoroutineScheduler to a TestDispatcher, and you
can pass a TestDispatcher to a TestScope
 */
class ActivityRepositoryImplTest {
    // test that getNewActivity() returns a result after switching the context
    @Test
    fun `getNewActivity() returns a result after switching the context`() = runTest {
        // Arrange
        val activityRepository = ActivityRepositoryImpl(
            appScope = this, // TestScope, provided by the runTest coroutine builder
            ioDispatcher = StandardTestDispatcher(testScheduler), // TestDispatcher
            remoteDataSource = FakeActivityRemoteDataSource(),
            localDataSource = FakeActivityLocalDataSource()
        )

        val expectedActivity = activity1

        // Act
        val result = activityRepository.getNewActivity()

        // Assert
        assert(result is Result.Success)
        assertEquals((result as Result.Success).data, expectedActivity)
    }
}