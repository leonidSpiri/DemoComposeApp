package ru.spiridonov.myapplication.data.model.comments_dto

import com.google.gson.annotations.SerializedName

data class CommentsDto(
    @SerializedName("id") val id: Long,
    @SerializedName("from_id") val fromProfileId: Long,
    @SerializedName("text") val commentText: String,
    @SerializedName("date") val publicationDate: Long,
)