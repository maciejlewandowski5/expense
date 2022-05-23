package com.example.e.split

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.e.InputWrapperCard
import com.example.e.R
import com.example.e.domain.Participant
import com.example.e.ui.theme.debt
import com.example.e.ui.theme.loan

@Composable
fun SplitContent(
    payers: List<CustomSplitParticipant>,
    borrowers: List<CustomSplitParticipant>,
    errorMessage: String?,
    loadingBar: Boolean = false,
    leftToSplit: String?,
    setBorrowerAmount: (Participant, String) -> Unit,
    setPayerAmount: (Participant, String) -> Unit,
    createExpense: () -> Unit,
    totalBorrowedAmount: String
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { createExpense() }) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.addNewExpense)
                )
            }
        },
        content = { _ ->
            InputWrapperCard(
                errorMessage = errorMessage,
                loadingBar = loadingBar,
                spacedBy = 2,
                content = {
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxHeight(0.1f)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                color = MaterialTheme.colors.onBackground,
                                text = stringResource(id = R.string.totalBorrowedAmount),
                            )
                            Text(
                                color = MaterialTheme.colors.onBackground,
                                text = "$totalBorrowedAmount ${stringResource(R.string.PLN)}",
                            )
                        }
                        Row(
                            horizontalArrangement = Arrangement.End,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            if (leftToSplit != null) {
                                Text(
                                    color = MaterialTheme.colors.onBackground,
                                    text = if (leftToSplit.startsWith("-")) stringResource(id = R.string.payersShouldPay) else {
                                        stringResource(id = R.string.borrowesShouldBorrow)
                                    },
                                )
                                Text(
                                    color = MaterialTheme.colors.onBackground,
                                    text = "${leftToSplit.removePrefix("-")} ${stringResource(R.string.PLN)}",
                                )
                            } else {
                                Text(
                                    color = MaterialTheme.colors.onBackground,
                                    text = stringResource(id = R.string.amountSplitedCorrectly),
                                )
                            }
                        }
                    }
                    Column(modifier = Modifier.fillMaxHeight()) {
                        SplitCard(
                            borrowers,
                            stringResource(R.string.borowers),
                            sign = "-",
                            color = MaterialTheme.colors.debt,
                            amountSet = setBorrowerAmount,
                            modifier = Modifier.fillMaxHeight(0.5f)
                        )
                        SplitCard(
                            payers,
                            stringResource(R.string.payers),
                            sign = "+",
                            color = MaterialTheme.colors.loan,
                            amountSet = setPayerAmount,
                            modifier = Modifier.fillMaxHeight()
                        )
                    }
                }
            )
        }
    )
}

@Composable
private fun SplitCard(
    users: List<CustomSplitParticipant>,
    text: String,
    modifier: Modifier = Modifier,
    sign: String,
    color: Color,
    amountSet: (Participant, String) -> Unit
) {
    Column(verticalArrangement = Arrangement.Top, modifier = modifier.fillMaxHeight()) {
        Box(
            contentAlignment = Alignment.BottomStart,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                color = MaterialTheme.colors.onBackground,
                text = text,
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(0.3f)

            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxHeight()
                .defaultMinSize(minHeight = 200.dp)
        ) {
            val shape = RoundedCornerShape(8.dp)
            items(users.size) { index ->

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation = 4.dp, shape = shape)
                        .background(MaterialTheme.colors.surface)
                        .clip(shape)
                        .padding(vertical = 16.dp, horizontal = 24.dp)
                ) {
                    Text(
                        text = users[index].participant.user.name,
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier.fillMaxWidth(0.2f),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Row(
                        Modifier.fillMaxWidth(0.8f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = sign,
                            color = color,
                            overflow = TextOverflow.Clip,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        TextField(
                            value = users[index].amountInput.removePrefix("-"),
                            { amountSet(users[index].participant, it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            maxLines = 1,
                            modifier = Modifier.fillMaxWidth(0.8f),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = color,
                                focusedLabelColor = color,
                                cursorColor = color
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
