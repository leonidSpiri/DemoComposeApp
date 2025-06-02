package ru.spiridonov.myapplication.ui.views.news_feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.id.VKID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.spiridonov.myapplication.data.mapper.NewsFeedMapper
import ru.spiridonov.myapplication.data.network.ApiFactory
import ru.spiridonov.myapplication.domain.FeedPost
import ru.spiridonov.myapplication.domain.StatisticItem

class NewsFeedViewModel : ViewModel() {


    private val initialState = NewsFeedScreenState.Initial

    private val _screenState = MutableLiveData<NewsFeedScreenState>(initialState)
    val screenState: LiveData<NewsFeedScreenState> = _screenState

    private val mapper = NewsFeedMapper()

    init {
        loadRecommendation()
    }

    private fun loadRecommendation() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = ApiFactory.apiService.loadRecommendations(token = VKID.Companion.instance.accessToken?.token.toString())
            val feedPosts = mapper.mapNewsFeedResponseToPosts(response)
            _screenState.value = NewsFeedScreenState.Posts(feedPosts)
        }
    }

    fun updateCount(feedPost: FeedPost, item: StatisticItem) {
        val currentState = screenState.value
        if (currentState !is NewsFeedScreenState.Posts) return
        val oldPosts = currentState.posts.toMutableList()
        val oldStatisticItem = feedPost.statisticsList
        val newStatistics = oldStatisticItem.toMutableList().apply {
            replaceAll { oldItem ->
                if (oldItem.type == item.type)
                    oldItem.copy(count = oldItem.count + 1)
                else oldItem
            }
        }
        val newFeedPost = feedPost.copy(statisticsList = newStatistics)
        val newPosts = oldPosts.apply {
            replaceAll {
                if (it.id == feedPost.id) newFeedPost
                else it
            }
        }
        _screenState.value = NewsFeedScreenState.Posts(posts = newPosts)
    }

    fun remove(feedPost: FeedPost) {
        val currentState = screenState.value
        if (currentState !is NewsFeedScreenState.Posts) return
        val oldPosts = currentState.posts.toMutableList()
        oldPosts.remove(feedPost)
        _screenState.value = NewsFeedScreenState.Posts(posts = oldPosts)
    }
}