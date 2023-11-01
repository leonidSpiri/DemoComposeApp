package ru.spiridonov.myapplication.ui.views.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.spiridonov.myapplication.domain.FeedPost
import ru.spiridonov.myapplication.domain.PostComment

class CommentsViewModel : ViewModel() {
    private val _screenState = MutableLiveData<CommentsScreenState>(CommentsScreenState.Initial)
    val screenState: LiveData<CommentsScreenState> = _screenState

    init {
        loadComments(FeedPost())
    }

    fun loadComments(feedPost: FeedPost) {
        val commentsList = mutableListOf<PostComment>().apply {
            repeat(10) {
                add(
                    PostComment(
                        id = it
                    )
                )
            }
        }
        _screenState.value =
            CommentsScreenState.Comments(feedPost = feedPost, comments = commentsList)
    }
}