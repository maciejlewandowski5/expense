package com.example.e.source

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.e.R

@Composable
fun SwitchSourceCard(
    isSourceRemote: Boolean,
    contract: SwitchSourceCardContract
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        Text(
            text = stringResource(id = R.string.onlineSource),
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onBackground,
            overflow = TextOverflow.Clip,
            maxLines = 1
        )
        Switch(checked = isSourceRemote, onCheckedChange = {
            contract.switchSource(it)
            if (it) contract.onSourceSwitchedToTrue()
            else contract.onSourceSwitchedToFalse()
        })
    }
}
