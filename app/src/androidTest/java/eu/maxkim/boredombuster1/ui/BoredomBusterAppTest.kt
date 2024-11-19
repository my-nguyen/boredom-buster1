package eu.maxkim.boredombuster1.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import eu.maxkim.boredombuster1.R
import eu.maxkim.boredombuster1.Tags
import eu.maxkim.boredombuster1.activity.framework.datasource.responseAndroidActivity1
import eu.maxkim.boredombuster1.activity.framework.datasource.responseAndroidActivity2
import eu.maxkim.boredombuster1.framework.AppDatabase
import eu.maxkim.boredombuster1.successfulAndroidResponse1
import eu.maxkim.boredombuster1.successfulAndroidResponse2
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class BoredomBusterAppTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    // order = 2 to ensure that composeTestRule is 2nd to hiltRule
    // create an AndroidComposeTestRule for testing Android activities
    // When using createAndroidComposeRule() we don't need to call setContent() ourselves anymore,
    // since it will create our MainActivity for us.
    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var mockWebServer: MockWebServer

    @Inject
    lateinit var database: AppDatabase

    @Before
    fun init() {
        // need to call hiltRule.inject() to populate the @Inject properties
        hiltRule.inject()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        database.close()
    }

    @Test
    fun refreshingSavingAndDeletingWorksCorrectly() {
        enqueueActivityResponse(successfulAndroidResponse1)
        waitUntilVisibleWithText(responseAndroidActivity1.name)

        enqueueActivityResponse(successfulAndroidResponse2)
        refreshActivity()
        waitUntilVisibleWithText(responseAndroidActivity2.name)

        saveAsFavorite()
        navigateToFavorites()

        composeTestRule.onNodeWithText(responseAndroidActivity2.name)
            .assertIsDisplayed()

        deleteFromFavorites()

        composeTestRule.onNodeWithText(responseAndroidActivity2.name)
            .assertDoesNotExist()

        val noActivitiesMessage = ApplicationProvider.getApplicationContext<Context>()
            .getString(R.string.message_empty_activity_list)

        composeTestRule.onNodeWithText(noActivitiesMessage)
            .assertIsDisplayed()
    }

    // enqueue an activity json on our MockWebServer
    private fun enqueueActivityResponse(activityJson: String) {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .setBody(activityJson)
        )
    }

    private fun navigateToFavorites() {
        composeTestRule.onNodeWithTag(Tags.FavoritesTab)
            .performClick()
    }

    private fun navigateToActivity() {
        composeTestRule.onNodeWithTag(Tags.ActivityTab)
            .performClick()
    }

    private fun saveAsFavorite() {
        clickOnNodeWithContentDescription(R.string.cd_save_activity)
    }

    private fun deleteFromFavorites() {
        clickOnNodeWithContentDescription(R.string.cd_delete_activity)
    }

    private fun refreshActivity() {
        clickOnNodeWithContentDescription(R.string.cd_refresh_activity)
    }

    // a method that clicks on a node with a content description
    private fun clickOnNodeWithContentDescription(@StringRes cdRes: Int) {
        val contentDescription = ApplicationProvider.getApplicationContext<Context>()
            .getString(cdRes)

        composeTestRule.onNodeWithContentDescription(contentDescription)
            .performClick()
    }

    // wait around until a node with activity's name appears on the screen
    private fun waitUntilVisibleWithText(text: String) {
        // waitUntil() is a convenient ComposeTestRule method to wait until the activity card
        // appears on the screen after loading
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithText(text)
                .fetchSemanticsNodes().size == 1
        }
    }
}

