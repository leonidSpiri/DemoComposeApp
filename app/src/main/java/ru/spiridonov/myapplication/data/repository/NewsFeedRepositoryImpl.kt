package ru.spiridonov.myapplication.data.repository

import android.app.Application
import com.vk.id.VKID
import ru.spiridonov.myapplication.data.mapper.NewsFeedMapper
import ru.spiridonov.myapplication.data.network.ApiFactory
import ru.spiridonov.myapplication.domain.FeedPost

class NewsFeedRepositoryImpl(application: Application) {
    private val token = updateToken()
    private val apiService = ApiFactory.apiService
    private val newsFeedMapper = NewsFeedMapper()


    suspend fun loadRecommendation(): List<FeedPost> {
        val response = apiService.loadRecommendations(token = token)
        val feedPosts = newsFeedMapper.mapNewsFeedResponseToPosts(response)
        return feedPosts
    }

    suspend fun addLike(feedPost: FeedPost) {
        val ownerId = -feedPost.communityId

        val response = apiService.addLike(
            token = token,
            ownerId = ownerId,
            postId = feedPost.id
        )

        //if (response != null) changeLikeInFeedPosts(response, feedPost)
    }

    suspend fun deleteLike(feedPost: FeedPost) {
        val ownerId = -feedPost.communityId

        val response = apiService.deleteLike(
            token = token,
            ownerId = ownerId,
            postId = feedPost.id,
        )

       // if (response != null) changeLikeInFeedPosts(response, feedPost)
    }


    private fun updateToken() =
        VKID.Companion.instance.accessToken?.token
            ?: throw IllegalStateException("token is null")

}