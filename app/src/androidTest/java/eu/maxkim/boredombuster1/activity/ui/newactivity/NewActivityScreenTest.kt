package eu.maxkim.boredombuster1.activity.ui.newactivity

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import eu.maxkim.boredombuster1.R
import eu.maxkim.boredombuster1.Tags
import eu.maxkim.boredombuster1.activity.framework.datasource.androidActivity1
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class NewActivityScreenTest {

    // ComposeTestRule allows us to set composable content on the screen of our test device
    @get:Rule
    val composeTestRule = createComposeRule()

    // test that the activity name is displayed correctly on the card
    @Test
    fun activityNameDisplayedOnACard() {
        composeTestRule.setContent {
            NewActivityCard(
                modifier = Modifier.fillMaxWidth(),
                activity = androidActivity1,
                isFavorite = false,
                onFavoriteClick = { },
                onLinkClick = { }
            )
        }

        // we will be using the Semantics tree to look up components on the screen and make
        // assertions about them.
        // ComposeTestRule has a series of finder onNode() methods that return a
        // SemanticsNodeInteraction, on which we can make assertions and perform actions.
        // For finding a node with a static text we can use ComposeTestRule's onNodeWithText()
        // method. After that we will call assertIsDisplayed() on it.
        composeTestRule.onNodeWithText(androidActivity1.name)
            .assertIsDisplayed()
    }

    // test that the Favorite button triggers the onFavoriteClick function
    @Test
    fun onFavoriteClickCallbackIsTriggered() {
        // for interaction with components, especially lambdas, the easiest route is to use Mockito
        val onFavoriteClick: (isFavorite: Boolean) -> Unit = mock()
        val isFavorite = false

        composeTestRule.setContent {
            NewActivityCard(
                modifier = Modifier.fillMaxWidth(),
                activity = androidActivity1,
                isFavorite = isFavorite,
                onFavoriteClick = onFavoriteClick,
                onLinkClick = { }
            )
        }

        // The content descriptions are stored in the strings.xml file, so we will need a Context to
        // access them
        val contentDescription = ApplicationProvider.getApplicationContext<Context>()
            .getString(R.string.cd_save_activity)

        // not all UI components will have text on them, so we need other means to access their
        // SemanticsNodeInteraction. the favorite button doesn't have any text, but it does have a
        // content description. There is another way how we can access a node - with ComposeTestRule
        // onNodeWithContentDescription() method.
        composeTestRule.onNodeWithContentDescription(contentDescription)
            .performClick()

        verify(onFavoriteClick, times(1)).invoke(!isFavorite)
    }

    // test if clicking a link on the activity card triggers the onLinkClick callback.
    @Test
    fun onLinkClickCallbackIsTriggered() {
        val onLinkClick: (link: String) -> Unit = mock()

        composeTestRule.setContent {
            NewActivityCard(
                modifier = Modifier.fillMaxWidth(),
                activity = androidActivity1,
                isFavorite = false,
                onFavoriteClick = { },
                onLinkClick = onLinkClick
            )
        }

        // use a testTag to find the TextButton composable as a node in the semantics tree
        composeTestRule.onNodeWithTag(Tags.ActivityLink)
            .performClick()

        verify(onLinkClick, times(1)).invoke(androidActivity1.link)
    }
}
