package eu.maxkim.boredombuster1.activity.framework.datasource

import com.squareup.moshi.Moshi
import eu.maxkim.boredombuster1.activity.framework.api.ActivityApiClient
import eu.maxkim.boredombuster1.activity.framework.api.ActivityTypeAdapter
import eu.maxkim.boredombuster1.model.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@ExperimentalCoroutinesApi
class ActivityRemoteDataSourceImplTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiClient: ActivityApiClient

    private val client = OkHttpClient.Builder().build()

    private val moshi: Moshi = Moshi.Builder()
        .add(ActivityTypeAdapter())
        .build()

    @Before
    fun createServer() {
        mockWebServer = MockWebServer()

        apiClient = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/")) // setting a dummy url
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .build()
            .create(ActivityApiClient::class.java)
    }

    @After
    fun shutdownServer() {
        mockWebServer.shutdown()
    }

    // test that the correct JSON is parsed successfully into an Activity
    @Test
    fun `correct response is parsed into success result`() = runTest {
        // Arrange
        // create a MockResponse that will return our prepared json body and enqueue it.
        val response = MockResponse()
            .setBody(successfulResponse)
            .setResponseCode(200)

        // enqueue() this response on our mockWebServer. that is all we need to start testing our
        // ActivityRemoteDataSource
        mockWebServer.enqueue(response)

        val activityRemoteDataSource = ActivityRemoteDataSourceImpl(apiClient)
        val expectedActivity = responseActivity

        // Act
        val result = activityRemoteDataSource.getActivity()

        // Assert
        assert(result is Result.Success)
        assertEquals((result as Result.Success).data, expectedActivity)
    }

}
