package ru.spiridonov.myapplication.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.spiridonov.myapplication.domain.FeedPost
import ru.spiridonov.myapplication.domain.StatisticItem

class MainViewModel : ViewModel() {

    private val _feedPost = MutableLiveData(FeedPost())
    val feedPost: LiveData<FeedPost> = _feedPost

    fun updateCount(item: StatisticItem) {
        val oldStatisticItem = feedPost.value?.statisticsList ?: throw IllegalStateException()
        val newStatistics = oldStatisticItem.toMutableList().apply {
            replaceAll { oldItem ->
                if (oldItem.type == item.type)
                    oldItem.copy(count = oldItem.count + 1)
                else oldItem
            }
        }
        _feedPost.value = _feedPost.value?.copy(statisticsList = newStatistics)
    }
}