package ru.spiridonov.myapplication.data.mapper

import ru.spiridonov.myapplication.data.model.news_feed_model_dto.NewsFeedResponseDto
import ru.spiridonov.myapplication.domain.FeedPost
import ru.spiridonov.myapplication.domain.StatisticItem
import ru.spiridonov.myapplication.domain.StatisticType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

class NewsFeedMapper {

    fun mapNewsFeedResponseToPosts(responseDto: NewsFeedResponseDto): List<FeedPost> {
        val result = mutableListOf<FeedPost>()

        val posts = responseDto.newsFeedContent.posts
        val groups = responseDto.newsFeedContent.groups

        posts.forEach { post ->

            val group =
                groups.find { it.id == post.communityId.absoluteValue } ?: return@forEach

            val feedPost = FeedPost(
                id = post.id,
                communityId = group.id,
                communityName = group.name,
                publicationDate = mapTimestampToDate(post.date * 1000),
                communityImageUrl = group.imageUrl,
                contentText = post.text,
                contentImageUrl = post.posts?.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url,
                statisticsList = listOf(
                    StatisticItem(type = StatisticType.LIKES, count = post.likes.count),
                    StatisticItem(type = StatisticType.COMMENTS, count = post.comments.count),
                    StatisticItem(type = StatisticType.VIEWS, count = post.views.count),
                    StatisticItem(type = StatisticType.SHARES, count = post.reposts.count)
                ),
                isFavourite = post.likes.userLikes > 0
            )

            result.add(feedPost)

        }

        return result
    }

    private fun mapTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp)
        return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }

}