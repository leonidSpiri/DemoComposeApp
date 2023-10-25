package ru.spiridonov.myapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.spiridonov.myapplication.domain.FeedPost
import ru.spiridonov.myapplication.domain.StatisticItem

class MainViewModel : ViewModel() {

    private val sourceList = mutableListOf<FeedPost>().apply {
        repeat(10) {
            add(
                FeedPost(
                    id = it
                )
            )
        }
    }

    private val _feedPosts = MutableLiveData<List<FeedPost>>(sourceList)
    val feedPosts: LiveData<List<FeedPost>> = _feedPosts

    fun updateCount(feedPost: FeedPost, item: StatisticItem) {
        val oldPosts = feedPosts.value?.toMutableList() ?: mutableListOf()
        val oldStatisticItem = feedPost.statisticsList
        val newStatistics = oldStatisticItem.toMutableList().apply {
            replaceAll { oldItem ->
                if (oldItem.type == item.type)
                    oldItem.copy(count = oldItem.count + 1)
                else oldItem
            }
        }
        val newFeedPost = feedPost.copy(statisticsList = newStatistics)
        _feedPosts.value = oldPosts.apply {
            replaceAll {
                if (it.id == feedPost.id) newFeedPost
                else it
            }
        }
    }

    fun remove(feedPost: FeedPost) {
        val oldPosts = feedPosts.value?.toMutableList() ?: mutableListOf()
        oldPosts.remove(feedPost)
        _feedPosts.value = oldPosts
    }
}