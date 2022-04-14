package com.example.e.main.group

import com.example.e.domain.AccountingGroup
import com.example.e.domain.GroupId

data class GroupCardData(
    val groups: List<AccountingGroup>,
    val currentGroupId: GroupId
) {

    fun currentGroupIndex() = groups.indexOfFirst { it.id == currentGroupId }
}
