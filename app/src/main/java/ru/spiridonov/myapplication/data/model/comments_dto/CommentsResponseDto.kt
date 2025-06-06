package ru.spiridonov.myapplication.data.model.comments_dto

import com.google.gson.annotations.SerializedName

data class CommentsResponseDto(
    @SerializedName("response") val commentsContent: CommentsContentDto,
)