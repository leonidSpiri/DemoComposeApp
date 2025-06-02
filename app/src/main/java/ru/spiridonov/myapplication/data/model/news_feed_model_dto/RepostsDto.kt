package ru.spiridonov.myapplication.data.model.news_feed_model_dto

import com.google.gson.annotations.SerializedName

data class RepostsDto(
    @SerializedName("count") val count: Int
)