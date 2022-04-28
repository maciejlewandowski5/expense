package com.example.e.expense

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.e.domain.Expense
import com.example.e.expense.ExpenseCard.ExpenseCard
import com.example.e.sampleExpense
import com.example.e.ui.theme.ETheme
import com.example.e.ui.theme.debt
import com.example.e.ui.theme.loan
import com.example.e.ui.theme.secondarySurface
import java.math.BigDecimal
import java.time.format.DateTimeFormatter

object ExpenseCard {
    @Composable
    fun ExpenseCard(expense: Expense, lighterBackground: Boolean, isExpanded: Boolean = false) {
        val backgroundColor = if (lighterBackground) {
            MaterialTheme.colors.background
        } else {
            MaterialTheme.colors.secondarySurface
        }
        var expanded by remember { mutableStateOf(isExpanded) }

        Surface(
            modifier = Modifier
                .background(backgroundColor)
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .clickable { expanded = !expanded }
                .scrollable(
                    ScrollableState { it },
                    Orientation.Horizontal,
                    enabled = true,
                    reverseDirection = false
                )
        ) {
            Row(
                modifier = Modifier
                    .background(backgroundColor)
                    .fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier
                        .background(backgroundColor)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(
                        modifier = Modifier
                            .align(alignment = Alignment.CenterVertically)
                            .animateContentSize()
                            .fillMaxWidth(0.6f)
                    ) { Title(expense, expanded) }
                    Column(modifier = Modifier.align(alignment = Alignment.CenterVertically)) {
                        Text(
                            text = expense.date.format(
                                DateTimeFormatter.ISO_DATE
                            ),
                            style = MaterialTheme.typography.caption,
                            modifier = Modifier.align(
                                alignment = Alignment.End
                            )
                        )
                        Text(
                            text = expense.totalAmount().toPlainString(),
                            style = MaterialTheme.typography.h5,
                            modifier = Modifier.align(alignment = Alignment.End),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = if (!expanded) 1 else Int.MAX_VALUE
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun Title(expense: Expense, expanded: Boolean) {
        Text(text = expense.title, style = MaterialTheme.typography.h6)
        Crossfade(targetState = expanded) { expanded ->
            if (!expanded) {
                Text(
                    text = expense.participants.map { it.user.name }
                        .fold("") { acc, s ->
                            if (acc.isNotEmpty()) {
                                "$acc, $s"
                            } else {
                                s
                            }
                        },
                    style = MaterialTheme.typography.caption,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    expense.participants.forEach {
                        val textColor = if (it.amount < BigDecimal.ZERO) {
                            MaterialTheme.colors.debt
                        } else {
                            MaterialTheme.colors.loan
                        }
                        Text(
                            text = "${it.user.name} ${it.amount}",
                            color = textColor,
                            style = MaterialTheme.typography.caption,
                            overflow = TextOverflow.Clip
                        )
                    }
                }
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
fun ExpensePreview() {
    ETheme {
        ExpenseCard(
            sampleExpense,
            true,
        )
    }
}
