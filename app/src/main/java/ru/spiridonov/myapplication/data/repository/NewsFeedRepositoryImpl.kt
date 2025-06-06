package ru.spiridonov.myapplication.data.repository

import android.app.Application
import com.vk.id.VKID
import ru.spiridonov.myapplication.data.mapper.NewsFeedMapper
import ru.spiridonov.myapplication.data.model.comments_dto.CommentsResponseDto
import ru.spiridonov.myapplication.data.model.news_feed_model_dto.LikesCountResponse
import ru.spiridonov.myapplication.data.network.ApiFactory
import ru.spiridonov.myapplication.domain.entity.FeedPost
import ru.spiridonov.myapplication.domain.entity.PostComment
import ru.spiridonov.myapplication.domain.entity.StatisticItem
import ru.spiridonov.myapplication.domain.entity.StatisticType

class NewsFeedRepositoryImpl(application: Application) {
    private val token = updateToken()
    private val apiService = ApiFactory.apiService
    private val newsFeedMapper = NewsFeedMapper()
    private var nextFrom: String? = null
    private val _feedPosts = mutableListOf<FeedPost>()
    val feedPostList: List<FeedPost>
        get() = _feedPosts.toList()


    suspend fun loadRecommendation(): List<FeedPost> {
        val startFrom = nextFrom
        if (startFrom.isNullOrEmpty() && feedPostList.isNotEmpty()) return feedPostList
        val response = if (startFrom.isNullOrEmpty())
            apiService.loadRecommendations(token = token)
        else
            apiService.loadRecommendations(token = token, startFrom)

        _feedPosts.addAll(newsFeedMapper.mapNewsFeedResponseToPosts(response))
        nextFrom = response.newsFeedContent.nextFrom
        return feedPostList
    }

    suspend fun addLike(feedPost: FeedPost) {
        val ownerId = -feedPost.communityId

        val response = apiService.addLike(
            token = token,
            ownerId = ownerId,
            postId = feedPost.id
        )
        if (response != null) changeLikeInFeedPosts(response, feedPost)
    }

    suspend fun deleteLike(feedPost: FeedPost) {
        val ownerId = -feedPost.communityId

        val response = apiService.deleteLike(
            token = token,
            ownerId = ownerId,
            postId = feedPost.id,
        )
        if (response != null) changeLikeInFeedPosts(response, feedPost)
    }

    private fun changeLikeInFeedPosts(
        response: LikesCountResponse,
        feedPost: FeedPost,
    ) {
        val newLikesCount = response.likesCountDto.count

        val newStatistics = feedPost.statisticsList.toMutableList().apply {
            removeIf { it.type == StatisticType.LIKES }
            add(StatisticItem(type = StatisticType.LIKES, count = newLikesCount))
        }

        val newPost = feedPost.copy(
            statisticsList = newStatistics, isLiked = !feedPost.isLiked
        )

        val postIndex = _feedPosts.indexOf(feedPost)

        if (postIndex != -1) _feedPosts[postIndex] = newPost
    }

    suspend fun removePost(feedPost: FeedPost) {
        val ownerId = -feedPost.communityId

        val postId = feedPost.id

        apiService.removeItem(
            token = token,
            ownerId = ownerId,
            postId = postId,
        )

        val feedPostItem = _feedPosts.find { it.id == feedPost.id }
        _feedPosts.remove(feedPostItem)

       // refreshedListFlow.emit(NewsFeedResult.Success(feedPostList))
    }

    suspend fun loadCommentsToPost(feedPost: FeedPost):List<PostComment> {
        val ownerId = -feedPost.communityId
        val response: CommentsResponseDto = apiService.getCommentsToPost(
            token = token,
            ownerId = ownerId,
            postId = feedPost.id
        )

        val comments = newsFeedMapper.mapCommentsResponseDtoToComments(response)
        return comments
        //commentsToPost.emit(comments)
    }

    private fun updateToken() =
        VKID.Companion.instance.accessToken?.token
            ?: throw IllegalStateException("token is null")

}