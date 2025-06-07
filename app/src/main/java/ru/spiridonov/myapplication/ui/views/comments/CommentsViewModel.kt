package ru.spiridonov.myapplication.ui.views.comments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.spiridonov.myapplication.data.repository.NewsFeedRepositoryImpl
import ru.spiridonov.myapplication.domain.entity.FeedPost

class CommentsViewModel(
    feedPost: FeedPost,
    application: Application
) : AndroidViewModel(application) {
    private val _screenState = MutableLiveData<CommentsScreenState>(CommentsScreenState.Initial)
    val screenState: LiveData<CommentsScreenState> = _screenState
    private val newsFeedRepositoryImpl = NewsFeedRepositoryImpl()

    init {
        loadComments(feedPost)
    }

    private fun loadComments(feedPost: FeedPost) {
        viewModelScope.launch(Dispatchers.IO) {
            val commentsList = newsFeedRepositoryImpl.loadCommentsToPost(feedPost)
            _screenState.value =
                CommentsScreenState.Comments(feedPost = feedPost, comments = commentsList)
        }
    }
}