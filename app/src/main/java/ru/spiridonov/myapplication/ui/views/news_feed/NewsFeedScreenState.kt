package ru.spiridonov.myapplication.ui.views.news_feed

import ru.spiridonov.myapplication.domain.entity.FeedPost

sealed class NewsFeedScreenState {
    data object Initial : NewsFeedScreenState()
    data object Loading: NewsFeedScreenState()
    data class Posts(val posts: List<FeedPost>, val nextDataIsLoading: Boolean = false) :
        NewsFeedScreenState()
}
