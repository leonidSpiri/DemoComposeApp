package ru.spiridonov.myapplication.data.model.comments_dto

import com.google.gson.annotations.SerializedName

data class CommentsContentDto(
    @SerializedName("items") val comments: List<CommentsDto>,
    @SerializedName("profiles") val profiles: List<ProfileDto>,
)