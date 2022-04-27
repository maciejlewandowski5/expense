package com.example.e.login.session

data class AccessToken(
    val token: String,
    val timeToLeave: Long
)
