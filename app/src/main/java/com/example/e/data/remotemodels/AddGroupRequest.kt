package com.example.e.data.remotemodels

import kotlinx.serialization.Serializable

@Serializable
data class AddGroupRequest(
    val accountingGroup: AccountingGroupRequest,
    val members: List<UserRequest>
)

@Serializable
data class AccountingGroupRequest(
    val id: Long,
    val title: String
)

@Serializable
data class UserRequest(
    val id: String,
    val name: String
)
