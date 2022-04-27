package com.example.e.data.remotemodels

import kotlinx.serialization.Serializable

@Serializable
data class AddUserRequest(
    val name: String
)
