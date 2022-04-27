package com.example.e.domain

import com.example.e.data.AccountingGroupModel

@JvmInline
value class GroupId(val value: Long) : DomainModel

data class AllGroups(val groups: List<AccountingGroup>) : DomainModel

data class AccountingGroup(
    val id: GroupId,
    val title: String
) : DomainModel {
    fun toAccountingGroupModel() = AccountingGroupModel(id = id.value, title = title)
}
