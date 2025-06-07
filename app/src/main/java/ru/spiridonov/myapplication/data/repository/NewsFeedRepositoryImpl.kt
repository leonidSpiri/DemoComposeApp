package ru.spiridonov.myapplication.data.repository

import com.vk.id.VKID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import ru.spiridonov.myapplication.data.mapper.NewsFeedMapper
import ru.spiridonov.myapplication.data.model.comments_dto.CommentsResponseDto
import ru.spiridonov.myapplication.data.model.news_feed_model_dto.LikesCountResponse
import ru.spiridonov.myapplication.data.network.ApiFactory
import ru.spiridonov.myapplication.domain.entity.FeedPost
import ru.spiridonov.myapplication.domain.entity.PostComment
import ru.spiridonov.myapplication.domain.entity.StatisticItem
import ru.spiridonov.myapplication.domain.entity.StatisticType
import ru.spiridonov.myapplication.extensions.mergeWith

class NewsFeedRepositoryImpl() {
    private val token = updateToken()
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)
    private val refreshedListFlow = MutableSharedFlow<List<FeedPost>>()

    private val loadedListFlow = flow {
        nextDataNeededEvents.emit(Unit)
        nextDataNeededEvents.collect {
            val startFrom = nextFrom

            if (startFrom == null && feedPosts.isNotEmpty()) {
                emit(feedPosts)
                return@collect
            }

            val response = if (startFrom == null) {
                apiService.loadRecommendations(token)
            } else {
                apiService.loadRecommendations(token, startFrom)
            }
            nextFrom = response.newsFeedContent.nextFrom
            val posts = mapper.mapNewsFeedResponseToPosts(response)
            _feedPosts.addAll(posts)
            emit(feedPosts)
        }
    }

    private val apiService = ApiFactory.apiService
    private val mapper = NewsFeedMapper()

    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private var nextFrom: String? = null

    val recommendations: StateFlow<List<FeedPost>> = loadedListFlow
        .mergeWith(refreshedListFlow)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = feedPosts
        )

    suspend fun loadNextData() {
        nextDataNeededEvents.emit(Unit)
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

        refreshedListFlow.emit(feedPosts)
    }

    suspend fun loadCommentsToPost(feedPost: FeedPost): List<PostComment> {
        val ownerId = -feedPost.communityId
        val response: CommentsResponseDto = apiService.getCommentsToPost(
            token = token,
            ownerId = ownerId,
            postId = feedPost.id
        )

        val comments = mapper.mapCommentsResponseDtoToComments(response)
        return comments
        //commentsToPost.emit(comments)
    }


    private fun updateToken() =
        VKID.Companion.instance.accessToken?.token
            ?: throw IllegalStateException("token is null")

}