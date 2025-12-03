package com.example.soundlink.core.domain.model

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val password: String,
    val age: Int,
    val avatarUrl: String,
    val verified: Boolean
){
    constructor() : this(0, "", "", "", 0, "", false)
    //Only name, email, password, age
    constructor(name: String, email: String, password: String, age: Int) : this(0, name, email, password, age, "", false)
}
