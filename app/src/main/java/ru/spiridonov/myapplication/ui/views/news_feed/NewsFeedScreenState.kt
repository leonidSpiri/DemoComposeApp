package ru.spiridonov.myapplication.ui.views.news_feed

import ru.spiridonov.myapplication.domain.FeedPost

sealed class NewsFeedScreenState{
    data object Initial: NewsFeedScreenState()
    data class Posts(val posts:List<FeedPost>): NewsFeedScreenState()
}
