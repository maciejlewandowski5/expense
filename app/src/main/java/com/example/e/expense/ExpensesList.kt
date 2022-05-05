package com.example.e.expense

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.e.DotsPulsing
import com.example.e.R
import com.example.e.domain.Expense
import com.example.e.sampleExpenses
import com.example.e.ui.theme.ETheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import io.iamjosephmj.flinger.bahaviours.StockFlingBehaviours
import io.iamjosephmj.flinger.flings.flingBehavior

object ExpensesList {
    @Composable
    fun ExpensesCard(
        expenses: ExpensesState,
        isRefreshing: Boolean,
        onRefresh: () -> Unit,
        deleteExpense: (Expense) -> Unit
    ) {
        when (expenses) {
            is ExpensesState.Success -> {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing),
                    onRefresh = { onRefresh() }
                ) {
                    LazyColumn(flingBehavior = StockFlingBehaviours.smoothScroll()) {
                        itemsIndexed(items = expenses.expenses, key = { _, item ->
                            item.id
                        }) { index, item ->
                            Column {
                                ExpenseCard.ExpenseCard(
                                    item,
                                    index % 2 == 0,
                                    delete = deleteExpense
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }
                    }
                }
            }
            ExpensesState.Error -> Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(text = stringResource(id = R.string.tryAgainLaterError))
            }
            ExpensesState.Loading -> Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                DotsPulsing()
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Default Dark Mode"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    name = "Default Light Mode"
)
@Composable
fun ExpensesPreview() {
    ETheme {
        ExpensesList.ExpensesCard(
            ExpensesState.Success(sampleExpenses), true, {}
        ) {}
    }
}
