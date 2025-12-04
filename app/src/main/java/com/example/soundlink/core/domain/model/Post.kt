package com.example.soundlink.core.domain.model

data class Post(
    val id: Long,
    val user: User,
    val title: String,
    val description: String,
    val tags: List<String>,
    val likes: Int,
    val comments: Int,
    val shares: Int,
    val timestamp: Long

    //Content like image or video URL are not stored here for brevity so we used a placeholder

){
    constructor(): this(
        id = 0,
        user = User(),
        title = "",
        description = "",
        tags = emptyList(),
        likes = 0,
        comments = 0,
        shares = 0,
        timestamp = 0
    )

    //Constructor for creating a post
    constructor(
        user: User,
        title: String,
        description: String,
        tags: List<String>,
        likes: Int,
        comments: Int,
        shares: Int,
        timestamp: Long
    ) : this(
        id = 0,
        user = user,
        title = title,
        description = description,
        tags = tags,
        likes = likes,
        comments = comments,
        shares = shares,
        timestamp = timestamp
    )
}
