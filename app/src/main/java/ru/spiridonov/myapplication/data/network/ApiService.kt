package ru.spiridonov.myapplication.data.network

import retrofit2.http.GET
import retrofit2.http.Query
import ru.spiridonov.myapplication.data.model.news_feed_model_dto.NewsFeedResponseDto

const val APP_API_VERSION = "v=5.199"
const val GET_RECOMMENDED = "newsfeed.getRecommended"

interface ApiService {

    @GET("$GET_RECOMMENDED?$APP_API_VERSION")
    suspend fun loadRecommendations(
        @Query("access_token") token: String,
    ): NewsFeedResponseDto


}