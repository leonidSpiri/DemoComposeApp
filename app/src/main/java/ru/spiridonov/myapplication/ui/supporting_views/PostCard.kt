package ru.spiridonov.myapplication.ui.supporting_views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.spiridonov.myapplication.R
import ru.spiridonov.myapplication.domain.FeedPost
import ru.spiridonov.myapplication.domain.StatisticItem
import ru.spiridonov.myapplication.domain.StatisticType

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    feedPost: FeedPost,
    onLikeClickListener: (StatisticItem) -> Unit,
    onShareClickListener: (StatisticItem) -> Unit,
    onViewsClickListener: (StatisticItem) -> Unit,
    onCommentClickListener: (StatisticItem) -> Unit
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            PostHeader(feedPost)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = feedPost.contentText)
            Spacer(modifier = Modifier.height(8.dp))
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                model = feedPost.contentImageUrl,
                contentDescription = null,
                contentScale = ContentScale.FillWidth
            )
            Spacer(modifier = Modifier.height(8.dp))
            Statistics(
                statisticsList = feedPost.statisticsList,
                onLikeClickListener = onLikeClickListener,
                onShareClickListener = onShareClickListener,
                onViewsClickListener = onViewsClickListener,
                onCommentClickListener = onCommentClickListener
            )
        }
    }
}

@Composable
private fun PostHeader(feedPost: FeedPost) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            model = feedPost.communityImageUrl,
            contentDescription = null
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = feedPost.communityName,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = feedPost.publicationDate,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondary
        )
    }
}


@Composable
private fun Statistics(
    statisticsList: List<StatisticItem>,
    onLikeClickListener: (StatisticItem) -> Unit,
    onShareClickListener: (StatisticItem) -> Unit,
    onViewsClickListener: (StatisticItem) -> Unit,
    onCommentClickListener: (StatisticItem) -> Unit
) {
    Row {
        Row(
            modifier = Modifier.weight(1f)
        ) {
            val viewsItem = statisticsList.getItemByType(StatisticType.VIEWS)
            IconWithText(
                iconResId = R.drawable.ic_views_count,
                text = viewsItem.count.toString(),
                onItemClickListener = { onViewsClickListener(viewsItem) })
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val sharesItem = statisticsList.getItemByType(StatisticType.SHARES)
            IconWithText(iconResId = R.drawable.ic_share, text = sharesItem.count.toString(),
                onItemClickListener = { onShareClickListener(sharesItem) })
            val commentItem = statisticsList.getItemByType(StatisticType.COMMENTS)
            IconWithText(iconResId = R.drawable.ic_comment, text = commentItem.count.toString(),
                onItemClickListener = { onCommentClickListener(commentItem) })
            val likeItem = statisticsList.getItemByType(StatisticType.LIKES)
            IconWithText(iconResId = R.drawable.ic_like, text = likeItem.count.toString(),
                onItemClickListener = { onLikeClickListener(likeItem) })
        }
    }
}

@Composable
private fun IconWithText(iconResId: Int, text: String, onItemClickListener: () -> Unit) {
    Row(
        modifier = Modifier.clickable {
            onItemClickListener()
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondary
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}

private fun List<StatisticItem>.getItemByType(type: StatisticType) =
    this.find { it.type == type } ?: throw IllegalStateException("type $this was not found")