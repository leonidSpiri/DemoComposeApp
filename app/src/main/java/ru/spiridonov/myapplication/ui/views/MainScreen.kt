package ru.spiridonov.myapplication.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.spiridonov.myapplication.domain.FeedPost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val feedPost = remember {
        mutableStateOf(FeedPost())
    }
    Scaffold(
        bottomBar = {
            NavigationBar {
                val selectedItemPosition = remember {
                    mutableStateOf(0)
                }

                val items =
                    listOf(NavigationItem.Home, NavigationItem.Favorite, NavigationItem.Profile)
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemPosition.value == index,
                        onClick = { selectedItemPosition.value = index },
                        icon = {
                            Icon(item.icon, contentDescription = null)
                        },
                        label = {
                            Text(text = stringResource(id = item.titleResId))
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
                            unselectedTextColor = MaterialTheme.colorScheme.onSecondary,
                            indicatorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }

    ) {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            PostCard(
                modifier = Modifier
                    .padding(4.dp),
                feedPost = feedPost.value,
                onStatisticsItemClickListener = { newItem ->
                    val oldStatisticItem = feedPost.value.statisticsList
                    val newStatistics = oldStatisticItem.toMutableList().apply {
                        replaceAll { oldItem ->
                            if (oldItem.type == newItem.type)
                                oldItem.copy(count = oldItem.count + 1)
                            else oldItem
                        }
                    }
                    feedPost.value = feedPost.value.copy(statisticsList = newStatistics)
                }
            )
        }
    }
}