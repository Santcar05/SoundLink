package com.example.soundlink.core.domain.model

data class User (
    val id: Long?,
    val name: String,
    val email: String,
    val password: String,
    val age: Long
){
    constructor() : this(null, "", "", "", 0)
    constructor( name: String, email: String, password: String, age: Long) : this(null, name, email, password, age)
}