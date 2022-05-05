package com.example.e.addexpense.view

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import com.example.e.R
import com.example.e.addexpense.*
import com.example.e.addexpense.model.AddExpenseEffect
import com.example.e.addexpense.model.NewExpenseInput
import com.example.e.addexpense.participantpicker.ParticpantCardState
import java.time.LocalDateTime

@Composable
fun AddExpenseScreen(
    addExpenseViewModel: AddExpenseViewModel,
    showDatePicker: () -> Unit,
    navigateToHome: (NewExpenseInput) -> Unit
) {
    val borrowersState by addExpenseViewModel.borrowersState.observeAsState(emptyList())
    val payersState by addExpenseViewModel.payersState.observeAsState(emptyList())
    val date by addExpenseViewModel.expenseDateTime.observeAsState(initial = LocalDateTime.now())
    val title by addExpenseViewModel.title.observeAsState(initial = "")
    val addExpenseEffect by addExpenseViewModel.addExpenseEffect.observeAsState(initial = null)
    val (showLoadingBar, errorMessage: String?) = resolveAddExpenseEffect(
        addExpenseEffect, navigateToHome
    )

    AddExpenseScaffold(
        addExpenseViewModel = addExpenseViewModel,
        title = title,
        showDatePicker = showDatePicker,
        date = date,
        borrowersState = borrowersState,
        payersState = payersState,
        errorMessage = errorMessage,
        loadingBar = showLoadingBar
    )
}

@Composable
private fun resolveAddExpenseEffect(
    addExpenseEffect: AddExpenseEffect?,
    navigateToHome: (NewExpenseInput) -> Unit
) = when (addExpenseEffect) {
    is AddExpenseEffect.ShowProgressBar ->
        Pair(true, null)
    is AddExpenseEffect.ShowGenericErrorMessage<*> ->
        Pair(false, stringResource(id = R.string.genericError))
    is AddExpenseEffect.ShowInputErrorMessage ->
        Pair(false, stringResource(id = addExpenseEffect.value.errorMessage()))
    is AddExpenseEffect.GoToSplitScreen ->
        Pair(
            false,
            null
        ).also { LaunchedEffect(key1 = Unit) { navigateToHome(addExpenseEffect.newExpenseInput) } }
    else ->
        Pair(false, null)
}

@Composable
private fun AddExpenseScaffold(
    addExpenseViewModel: AddExpenseViewModel,
    title: String,
    showDatePicker: () -> Unit,
    date: LocalDateTime,
    borrowersState: List<ParticpantCardState>,
    payersState: List<ParticpantCardState>,
    errorMessage: String? = null,
    loadingBar: Boolean
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = addExpenseViewModel::addExpenseClick) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.addNewExpense)
                )
            }
        },
        content = { _ ->
            AddExpenseContent(
                title = title,
                date = date,
                payersState = payersState,
                borrowersState = borrowersState,
                contract = addExpenseViewModel,
                showDatePicker = showDatePicker,
                errorMessage = errorMessage,
                loadingBar = loadingBar
            )
        }
    )
}
