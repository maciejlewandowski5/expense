package com.example.e.main.spendings

import com.example.e.main.group.GroupCardData
import java.math.BigDecimal

data class HeaderCardData(
    val amount: BigDecimal,
    val currency: String,
    val groupCardData: GroupCardData
) {
    fun formattedAmount() = "$amount $currency"
}

sealed class CurrentSpendingState() {
    object Loading : CurrentSpendingState()
    class Success(val headerCardData: HeaderCardData) : CurrentSpendingState()
    object Error : CurrentSpendingState()
}
