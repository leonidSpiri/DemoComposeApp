package ru.spiridonov.myapplication.domain

import ru.spiridonov.myapplication.R

data class FeedPost(
    val id: Int = 0,
    val communityName: String = "/dev/null",
    val publicationDate: String = "14:54",
    val avatarResId: Int = R.drawable.post_comunity_thumbnail,
    val contentText: String = "Donec vitae nunc tortor. Interdum.",
    val contentImageResId: Int = R.drawable.post_content_image,
    val statisticsList: List<StatisticItem> = listOf(
        StatisticItem(StatisticType.VIEWS, 966),
        StatisticItem(StatisticType.SHARES, 32),
        StatisticItem(StatisticType.COMMENTS, 55),
        StatisticItem(StatisticType.LIKES, 10000),
    )
)
