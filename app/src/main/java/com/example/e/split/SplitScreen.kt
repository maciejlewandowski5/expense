package com.example.e.split

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import com.example.e.R
import com.example.e.addexpense.model.AddExpenseEffect

@Composable
fun SplitScreen(
    splitViewModel: SplitViewModel,
    navigateToHome: () -> Unit
) {
    val leftToSplit by splitViewModel.amountToSplit.observeAsState("0")
    val totalBorrowedAmount by splitViewModel.totalExpenseAmount.observeAsState("0")
    val errorMessage by splitViewModel.errorMessage.observeAsState(null)
    val borrowers by splitViewModel.borrowersState.observeAsState(initial = emptyList())
    val payers by splitViewModel.payersState.observeAsState(initial = emptyList())
    val addExpenseEffect by splitViewModel.addExpenseEffect.observeAsState()
    val (showLoadingBar, errorMessage2: String?) = resolveAddExpenseEffect(
        addExpenseEffect, navigateToHome
    )
    SplitContent(
        borrowers = borrowers,
        payers = payers,
        errorMessage = errorMessage ?: errorMessage2,
        loadingBar = showLoadingBar,
        leftToSplit = leftToSplit,
        totalBorrowedAmount = totalBorrowedAmount,
        setBorrowerAmount = splitViewModel::setBorrowerAmount,
        setPayerAmount = splitViewModel::setPayerAmount,
        createExpense = splitViewModel::createExpense
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
