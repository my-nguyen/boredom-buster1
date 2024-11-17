package eu.maxkim.boredombuster1.activity.ui.newactivity

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import eu.maxkim.boredombuster1.activity.framework.datasource.androidActivity1
import org.junit.Rule
import org.junit.Test

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
}
