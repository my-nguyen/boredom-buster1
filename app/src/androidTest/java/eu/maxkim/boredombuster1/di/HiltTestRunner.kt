package eu.maxkim.boredombuster1.di

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import dagger.hilt.android.testing.HiltTestApplication

// a custom runner, which will set up this application for tests
class HiltTestRunner : AndroidJUnitRunner() {

    override fun newApplication(classLoader: ClassLoader?, name: String?, context: Context?): Application {
        // Since our app doesn't need anything from our own Application class, we can use
        // HiltTestApplication provided by hilt-android-testing library
        return super.newApplication(classLoader, HiltTestApplication::class.java.name, context)
    }
}
