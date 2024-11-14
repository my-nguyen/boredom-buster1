package eu.maxkim.boredombuster1.activity.ui.favorite

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import eu.maxkim.boredombuster1.R
import eu.maxkim.boredombuster1.activity.model.Activity

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    when (val uiState = viewModel.uiStateLiveData.observeAsState().value) {
        is FavoritesUiState.List -> ActivityList(
            modifier = modifier,
            activityList = uiState.activityList,
            onDeleteClick = { activity ->
                viewModel.deleteActivity(activity)
            }
        )
        is FavoritesUiState.Empty, null -> NoItemsInfo(modifier = modifier)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ActivityList(
    modifier: Modifier,
    activityList: List<Activity>,
    onDeleteClick: (Activity) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(activityList, key = { item -> item.key }) { activity ->
            ActivityCard(
                modifier = Modifier.animateItemPlacement(),
                activity = activity,
                onDeleteClick = onDeleteClick
            )
        }
    }
}

@Composable
fun ActivityCard(
    modifier: Modifier,
    activity: Activity,
    onDeleteClick: (Activity) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp),
        elevation = 2.dp
    ) {
        Row {
            Text(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .weight(1f)
                    .align(alignment = Alignment.CenterVertically),
                text = activity.name
            )
            IconButton(
                modifier = Modifier
                    .align(alignment = Alignment.CenterVertically),
                onClick = {
                    onDeleteClick(activity)
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = stringResource(id = R.string.cd_delete_activity)
                )
            }
        }

    }
}

@Composable
fun NoItemsInfo(
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(id = R.string.message_empty_activity_list),
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
        )
    }
}