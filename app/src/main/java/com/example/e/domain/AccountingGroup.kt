package com.example.e.domain

import com.example.e.data.AccountingGroupModel

@JvmInline
value class GroupId(val value: Long)

data class AccountingGroup(
    val id: GroupId,
    val title: String
) {
    fun toAccountingGroupModel() = AccountingGroupModel(id = id.value, title = title)
}
