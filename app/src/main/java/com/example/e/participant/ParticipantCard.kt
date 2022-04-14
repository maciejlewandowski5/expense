package com.example.e.participant

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.e.addexpense.participantpicker.ParticpantCardState

@Composable
fun ParticipantCard(
    state: ParticpantCardState,
    participantClick: (ParticpantCardState) -> Unit,
    tileColor: Color,
    textColor: Color
) {
    val resolveTargetColor = if (state.isSelected) {
        tileColor
    } else {
        Color.Transparent
    }
    val animatedColor by animateColorAsState(targetValue = resolveTargetColor)

    val resolveTextTargetColor = if (state.isSelected) {
        textColor
    } else {
        MaterialTheme.colors.onBackground
    }
    val animatedTextColor by animateColorAsState(targetValue = resolveTextTargetColor)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(4.dp)
            .clip(CircleShape)
            .clickable { participantClick(state) }
            .background(animatedColor)
            .border(
                width = 1.dp,
                color = tileColor,
                shape = CircleShape
            )
            .padding(8.dp)
    ) {
        Text(
            text = state.user.name,
            color = animatedTextColor
        )
    }
}
