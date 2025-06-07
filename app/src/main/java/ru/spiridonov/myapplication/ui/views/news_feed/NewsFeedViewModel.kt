package ru.spiridonov.myapplication.ui.views.news_feed

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import ru.spiridonov.myapplication.data.repository.NewsFeedRepositoryImpl
import ru.spiridonov.myapplication.domain.entity.FeedPost
import ru.spiridonov.myapplication.extensions.mergeWith

class NewsFeedViewModel(application: Application) : AndroidViewModel(application) {

    private val newsFeedRepositoryImpl = NewsFeedRepositoryImpl()
    private val recommendationsFlow = newsFeedRepositoryImpl.recommendations

    private val loadNextDataFlow = MutableSharedFlow<NewsFeedScreenState>()

    val screenState = recommendationsFlow
        .filter { it.isNotEmpty() }
        .map { NewsFeedScreenState.Posts(posts = it) as NewsFeedScreenState }
        .onStart { emit(NewsFeedScreenState.Loading) }
        .mergeWith(loadNextDataFlow)

    fun loadNextRecommendations() {
        viewModelScope.launch {
            loadNextDataFlow.emit(
                NewsFeedScreenState.Posts(
                    posts = recommendationsFlow.value,
                    nextDataIsLoading = true
                )
            )
            newsFeedRepositoryImpl.loadNextData()
        }
    }

    fun changeLikeStatus(feedPost: FeedPost?) {
        if (feedPost == null) return
        viewModelScope.launch(Dispatchers.IO) {
            if (feedPost.isLiked)
                newsFeedRepositoryImpl.deleteLike(feedPost)
            else
                newsFeedRepositoryImpl.addLike(feedPost)
        }
    }

    fun remove(feedPost: FeedPost) {
        viewModelScope.launch(Dispatchers.IO) {
            newsFeedRepositoryImpl.removePost(feedPost)
        }
    }
}