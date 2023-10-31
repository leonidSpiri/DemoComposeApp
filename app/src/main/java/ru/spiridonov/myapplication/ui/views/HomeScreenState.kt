package ru.spiridonov.myapplication.ui.views

import ru.spiridonov.myapplication.domain.FeedPost
import ru.spiridonov.myapplication.domain.PostComment

sealed class HomeScreenState{
    data object Initial:HomeScreenState()
    data class Posts(val posts:List<FeedPost>):HomeScreenState()
    data class Comments(val feedPost: FeedPost, val comments:List<PostComment>):HomeScreenState()

}
