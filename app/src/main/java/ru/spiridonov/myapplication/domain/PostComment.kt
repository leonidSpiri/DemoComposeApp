package ru.spiridonov.myapplication.domain

import ru.spiridonov.myapplication.R

data class PostComment(
    val id: Int,
    val authorName: String = "User",
    val authorAvatarUrl: Int = R.drawable.comment_author_avatar,
    val commentText: String = "Text",
    val publishDate: String = "12:58"
)
