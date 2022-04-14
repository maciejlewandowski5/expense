package com.example.e.addexpense.view

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.e.InputWrapperCard
import com.example.e.R
import com.example.e.addexpense.participantpicker.ParticpantCardState
import com.example.e.addexpense.participantpicker.PickerContent
import com.example.e.sampleDateTime
import com.example.e.sampleParticipantCardState
import com.example.e.ui.theme.ETheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun AddExpenseContent(
    title: String,
    date: LocalDateTime,
    amount: String,
    payersState: List<ParticpantCardState>,
    borrowersState: List<ParticpantCardState>,
    contract: AddExpenseContentContract,
    errorMessage: String?,
    loadingBar: Boolean = false,
    showDatePicker: () -> Unit,
) {
    InputWrapperCard(
        errorMessage = errorMessage,
        loadingBar = loadingBar,
        content = {
            AddExpenseInputs(
                title = title,
                date = date,
                amount = amount,
                payersState = payersState,
                borrowersState = borrowersState,
                contract = contract,
                showDatePicker = showDatePicker
            )
        }
    )
}

@Composable
fun AddExpenseInputs(
    title: String,
    date: LocalDateTime,
    amount: String,
    payersState: List<ParticpantCardState>,
    borrowersState: List<ParticpantCardState>,
    contract: AddExpenseContentContract,
    showDatePicker: () -> Unit,
) {
    TextField(
        label = { Text(text = stringResource(id = R.string.expenseTitle)) },
        value = title,
        onValueChange = { contract.setTitle(it) }
    )
    Box(
        modifier = Modifier.clickable { showDatePicker() }
    ) {
        TextField(
            label = { Text(text = stringResource(id = R.string.date)) },
            value = date.format(DateTimeFormatter.ISO_DATE_TIME),
            onValueChange = {},
            enabled = false,
        )
    }
    PickerContent(
        participants = payersState,
        participantClick = { contract.payerClick(it) },
        modifier = Modifier.padding(horizontal = 8.dp),
        text = stringResource(id = R.string.payers),
        MaterialTheme.colors.primary,
        MaterialTheme.colors.onPrimary
    )
    TextField(
        label = { Text(text = stringResource(id = R.string.amount)) },
        value = amount,
        onValueChange = { contract.setAmount(it) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    PickerContent(
        participants = borrowersState,
        participantClick = { contract.borrowerClick(it) },
        modifier = Modifier.padding(horizontal = 8.dp),
        text = stringResource(id = R.string.borowers),
        MaterialTheme.colors.secondary,
        MaterialTheme.colors.onSecondary
    )
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
fun DefaultPreview() {
    ETheme {
        AddExpenseContent(
            ",", sampleDateTime, "10.00", sampleParticipantCardState,
            sampleParticipantCardState,
            object : AddExpenseContentContract {
                override fun setTitle(newTitle: String) {}
                override fun borrowerClick(pickedParticipant: ParticpantCardState) {}
                override fun setAmount(newAmount: String) {}
                override fun payerClick(pickedPayer: ParticpantCardState) {}
            },
            "error", false, {}
        )
    }
}
