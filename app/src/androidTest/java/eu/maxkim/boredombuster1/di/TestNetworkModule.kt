package eu.maxkim.boredombuster1.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import eu.maxkim.boredombuster1.activity.framework.api.ActivityApiClient
import eu.maxkim.boredombuster1.activity.framework.api.ActivityTypeAdapter
import eu.maxkim.boredombuster1.di.annotation.BaseUrl
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

// this replaces the NetworkModule and provides an ActivityApiClient that uses the MockWebServer
@Module
@TestInstallIn(components = [SingletonComponent::class], replaces = [NetworkModule::class])
object TestNetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideMockServer(): MockWebServer {
        return MockWebServer()
    }

    /**
     * We need to jump through the hoops a bit to avoid NetworkOnMainThread exception in our UI tests.
     */
    @Provides
    @Singleton
    @BaseUrl
    fun provideBaseUrl(mockWebServer: MockWebServer): String {
        var url = ""
        val thread = Thread {
            // mockWebServer.url("/") is considered a network operation, and because Android doesn’t
            // allow network operations on main thread we need to perform this operation on another
            // thread
            url = mockWebServer.url("/").toString()
        }
        thread.start()
        thread.join()
        return url
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().add(ActivityTypeAdapter()).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi, @BaseUrl baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiClient(retrofit: Retrofit): ActivityApiClient {
        return retrofit.create(ActivityApiClient::class.java)
    }
}
