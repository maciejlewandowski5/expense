package com.example.e.amount

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.e.InputWrapperCard
import com.example.e.R
import com.example.e.addexpense.model.AddExpenseEffect
import com.example.e.addexpense.model.NewExpenseInput
import com.example.e.domain.Expense
import com.example.e.ui.theme.debt
import com.example.e.ui.theme.loan
import java.math.BigDecimal

@Composable
fun AmountScreen(
    amountViewModel: AmountViewModel,
    splitManually: (NewExpenseInput) -> Unit,
    navigateToHome: () -> Unit
) {
    val errorMessage by amountViewModel.errorMessage.observeAsState(null)
    val proposedExpense by amountViewModel.proposedExpense.observeAsState(null)
    val amount by amountViewModel.amount.observeAsState(BigDecimal.ZERO.toPlainString())
    val addExpenseEffect by amountViewModel.addExpenseEffect.observeAsState(null)
    val (isLoading, errorMessage2) = resolveAddExpenseEffect(addExpenseEffect, navigateToHome)

    AmountScaffolding(
        errorMessage = errorMessage ?: errorMessage2,
        loadingBar = isLoading,
        amount = amount,
        amountSet = amountViewModel::amountSet,
        proposedExpense = proposedExpense,
        splitManually = { splitManually(amountViewModel.expenseInput!!) },
        addExpense = amountViewModel::addExpense
    )
}

@Composable
private fun resolveAddExpenseEffect(
    addExpenseEffect: AddExpenseEffect?,
    navigateToHome: () -> Unit
) = when (addExpenseEffect) {
    is AddExpenseEffect.ShowProgressBar ->
        Pair(true, null)
    is AddExpenseEffect.ShowGenericErrorMessage<*> ->
        Pair(false, stringResource(id = R.string.genericError))
    is AddExpenseEffect.ShowInputErrorMessage ->
        Pair(false, stringResource(id = addExpenseEffect.value.errorMessage()))
    is AddExpenseEffect.GoToSplitScreen ->
        Pair(false, null).also { LaunchedEffect(key1 = Unit) { navigateToHome() } }
    else ->
        Pair(false, null)
}

@Composable
fun AmountScaffolding(
    errorMessage: String?,
    loadingBar: Boolean,
    amount: String,
    amountSet: (String) -> Unit,
    proposedExpense: Expense?,
    splitManually: () -> Unit,
    addExpense: () -> Unit
) {
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { proposedExpense?.let { addExpense() } }) {
            Icon(
                Icons.Filled.Add,
                contentDescription = stringResource(id = R.string.addNewExpense)
            )
        }
    }, content = { _ ->
        AmountContent(
            errorMessage = errorMessage,
            loadingBar = loadingBar,
            amount = amount,
            amountSet = amountSet,
            proposedExpense = proposedExpense,
            splitManually = splitManually
        )
    })
}

@Composable
fun AmountContent(
    errorMessage: String?,
    loadingBar: Boolean,
    amount: String,
    amountSet: (String) -> Unit,
    proposedExpense: Expense?,
    splitManually: () -> Unit
) {
    InputWrapperCard(errorMessage = errorMessage, loadingBar = loadingBar) {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = amount,
                onValueChange = amountSet,
                label = { Text(text = stringResource(id = R.string.amount)) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Crossfade(targetState = proposedExpense) {
                if (it != null) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(text = stringResource(id = R.string.proposedSplit))
                        LazyColumn(content = {
                            items(count = it.participants.size) { index ->
                                val participant = it.participants[index]
                                Text(
                                    text = participant.user.name,
                                    color = MaterialTheme.colors.onBackground
                                )
                                Text(
                                    text = "${participant.amount.toPlainString()} PLN",
                                    color = if (participant.amount.compareTo(
                                            BigDecimal.ZERO
                                        ) == 1
                                    ) MaterialTheme.colors.loan else MaterialTheme.colors.debt
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        })
                    }
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(onClick = { splitManually() }) {
                    Text(
                        text = stringResource(id = R.string.splitManually),
                        color = MaterialTheme.colors.onPrimary,
                        style = MaterialTheme.typography.button,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}
