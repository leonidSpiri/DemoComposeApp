package ru.spiridonov.myapplication.ui.views.news_feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.spiridonov.myapplication.data.repository.NewsFeedRepositoryImpl
import ru.spiridonov.myapplication.domain.entity.FeedPost

class NewsFeedViewModel(application: Application) : AndroidViewModel(application) {


    private val initialState = NewsFeedScreenState.Initial

    private val _screenState = MutableLiveData<NewsFeedScreenState>(initialState)
    val screenState: LiveData<NewsFeedScreenState> = _screenState

    private val newsFeedRepositoryImpl = NewsFeedRepositoryImpl(application)

    init {
        _screenState.value = NewsFeedScreenState.Loading
        loadRecommendation()
    }

    private fun loadRecommendation() {
        viewModelScope.launch(Dispatchers.IO) {
            _screenState.value =
                NewsFeedScreenState.Posts(newsFeedRepositoryImpl.loadRecommendation())
        }
    }

    fun loadNextRecommendations() {
        _screenState.value =
            NewsFeedScreenState.Posts(
                newsFeedRepositoryImpl.feedPostList,
                nextDataIsLoading = true
            )
       loadRecommendation()
    }

    fun changeLikeStatus(feedPost: FeedPost?) {
        if (feedPost == null) return
        viewModelScope.launch(Dispatchers.IO) {
            if (feedPost.isLiked)
                newsFeedRepositoryImpl.deleteLike(feedPost)
            else
                newsFeedRepositoryImpl.addLike(feedPost)
            _screenState.value =
                NewsFeedScreenState.Posts(newsFeedRepositoryImpl.feedPostList)
        }
    }

    fun remove(feedPost: FeedPost) {
        viewModelScope.launch(Dispatchers.IO) {
            newsFeedRepositoryImpl.removePost(feedPost)
        }
        _screenState.value = NewsFeedScreenState.Posts(posts = newsFeedRepositoryImpl.feedPostList)
    }
}