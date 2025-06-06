package ru.spiridonov.myapplication.ui.views.comments

import ru.spiridonov.myapplication.domain.entity.FeedPost
import ru.spiridonov.myapplication.domain.entity.PostComment

sealed class CommentsScreenState {
    data object Initial : CommentsScreenState()
    data class Comments(val feedPost: FeedPost, val comments: List<PostComment>) :
        CommentsScreenState()
}
