package com.example.e.expense

import android.content.res.Configuration
import android.text.format.DateUtils
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.e.R
import com.example.e.domain.Expense
import com.example.e.expense.ExpenseCard.ExpenseCard
import com.example.e.sampleExpense
import com.example.e.ui.theme.ETheme
import com.example.e.ui.theme.debt
import com.example.e.ui.theme.loan
import com.example.e.ui.theme.secondarySurface
import com.google.accompanist.pager.ExperimentalPagerApi
import java.math.BigDecimal
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalPagerApi::class)
object ExpenseCard {
    @Composable
    fun ExpenseCard(
        expense: Expense,
        lighterBackground: Boolean,
        isExpanded: Boolean = false,
        modifier: Modifier = Modifier,
        delete: (Expense) -> Unit
    ) {
        val backgroundColor = if (lighterBackground) {
            MaterialTheme.colors.background
        } else {
            MaterialTheme.colors.secondarySurface
        }
        var expanded by remember { mutableStateOf(isExpanded) }

        Column(
            modifier = modifier
                .background(backgroundColor)
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .clickable { expanded = !expanded }
                .scrollable(
                    ScrollableState { it },
                    Orientation.Horizontal,
                    enabled = true,
                )
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
                ) {
                    Title(expense, expanded)
                }
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Crossfade(targetState = expanded, modifier = Modifier.align(Alignment.End)) {
                        if (it) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .clickable { delete(expense) }
                                    .padding(4.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Close,
                                    contentDescription = stringResource(id = R.string.removeExpense),
                                    tint = MaterialTheme.colors.onBackground,
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Text(
                        text = if (expanded) {
                            expense.date.format(
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                            )
                        } else {
                            DateUtils.getRelativeTimeSpanString(
                                expense.date.atZone(ZoneId.systemDefault()).toInstant()
                                    .toEpochMilli(),
                                System.currentTimeMillis(),
                                DateUtils.SECOND_IN_MILLIS,
                                DateUtils.FORMAT_ABBREV_ALL
                            ).toString()
                        },
                        color = MaterialTheme.colors.onBackground,
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
                        color = MaterialTheme.colors.onBackground,
                        maxLines = if (!expanded) 1 else Int.MAX_VALUE
                    )
                }
            }
        }
    }
}

@Composable
private fun Title(expense: Expense, expanded: Boolean) {
    Text(
        text = expense.title,
        style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.onBackground
    )
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
                color = MaterialTheme.colors.onBackground,
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
                        overflow = TextOverflow.Clip,
                    )
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
            delete = {}
        )
    }
}
