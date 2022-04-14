package com.example.e.addexpense.participantpicker

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.e.participant.ParticipantCard
import com.example.e.sampleParticipantCardState
import com.example.e.ui.theme.ETheme

@Composable
fun PickerContent(
    participants: List<ParticpantCardState>,
    participantClick: (ParticpantCardState) -> Unit,
    modifier: Modifier = Modifier,
    text: String,
    tileColor: Color,
    textColor: Color
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = text,
            color = MaterialTheme.colors.onBackground,

        )
        LazyRow(
            content = {
                items(items = participants) { item ->
                    ParticipantCard(item, participantClick, tileColor, textColor)
                }
            }
        )
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
fun ParticipantsPickerPreview() {
    ETheme {
        PickerContent(
            sampleParticipantCardState,
            {},
            Modifier,
            "Payers",
            MaterialTheme.colors.secondary,
            MaterialTheme.colors.onSecondary
        )
    }
}
