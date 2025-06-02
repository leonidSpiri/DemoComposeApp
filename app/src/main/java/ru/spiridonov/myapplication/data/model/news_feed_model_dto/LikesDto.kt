package ru.spiridonov.myapplication.data.model.news_feed_model_dto

import com.google.gson.annotations.SerializedName

data class LikesDto(
    @SerializedName("count") val count: Int,
    @SerializedName("user_likes") val userLikes: Int,
)