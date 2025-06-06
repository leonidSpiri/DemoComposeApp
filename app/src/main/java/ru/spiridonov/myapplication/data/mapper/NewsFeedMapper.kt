package ru.spiridonov.myapplication.data.mapper

import ru.spiridonov.myapplication.data.model.comments_dto.CommentsResponseDto
import ru.spiridonov.myapplication.data.model.news_feed_model_dto.NewsFeedResponseDto
import ru.spiridonov.myapplication.domain.entity.FeedPost
import ru.spiridonov.myapplication.domain.entity.PostComment
import ru.spiridonov.myapplication.domain.entity.StatisticItem
import ru.spiridonov.myapplication.domain.entity.StatisticType
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
                publicationDate = mapTimestampToDate(post.date),
                communityImageUrl = group.imageUrl,
                contentText = post.text,
                contentImageUrl = post.posts?.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url,
                statisticsList = listOf(
                    StatisticItem(type = StatisticType.LIKES, count = post.likes.count),
                    StatisticItem(type = StatisticType.COMMENTS, count = post.comments.count),
                    StatisticItem(type = StatisticType.VIEWS, count = post.views.count),
                    StatisticItem(type = StatisticType.SHARES, count = post.reposts.count)
                ),
                isLiked = post.likes.userLikes > 0
            )

            result.add(feedPost)

        }

        return result
    }

    fun mapCommentsResponseDtoToComments(responseDto: CommentsResponseDto): List<PostComment> {
        val result = mutableListOf<PostComment>()

        val comments = responseDto.commentsContent.comments
        val profiles = responseDto.commentsContent.profiles

        comments.forEach { comment ->
            if (comment.commentText.isBlank()) return@forEach

            val profile = profiles.find { comment.fromProfileId == it.id } ?: return@forEach
            val authorName = "${profile.firstName} ${profile.lastName}"

            val postComment = PostComment(
                id = comment.id,
                authorName = authorName,
                avatarUrl = profile.photoUrl,
                commentText = comment.commentText,
                publicationDate = mapTimestampToDate(comment.publicationDate)
            )

            result.add(postComment)
        }


        return result
    }

    private fun mapTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp * 1000)
        return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }

}