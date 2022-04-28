package com.example.e.main.spendings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.e.R
import com.example.e.main.MainScreenNavigationContract
import com.example.e.main.MainViewContract
import com.example.e.main.group.GroupPicker
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.fade
import com.google.accompanist.placeholder.material.placeholder

@Composable
internal fun ErrorHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxWidth()
            .height(150.dp)
    ) {
        Text(text = stringResource(id = R.string.tryAgainLaterError))
    }
}

@Composable
internal fun HeaderCardContent(
    headerCardData: HeaderCardData,
    navigation: MainScreenNavigationContract,
    contract: MainViewContract,
    isRefreshing: Boolean
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .defaultMinSize(minHeight = 150.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        GroupPicker(
            groupCardData = headerCardData.groupCardData,
            navigation::goToAddNewGroup,
            contract::setCurrentGroup
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.currentSpendingsTitle),
                style = MaterialTheme.typography.subtitle1,
                overflow = TextOverflow.Clip,
                maxLines = 1
            )
        }
        Row(modifier = Modifier.align(alignment = Alignment.CenterHorizontally)) {
            Text(
                text = headerCardData.formattedAmount(),
                style = MaterialTheme.typography.h3,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )
        }
        if (headerCardData.groupCardData.groups.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .align(alignment = Alignment.End)
                    .clip(shape = CircleShape)
            ) {
                Button(
                    onClick = navigation::goToSettlement,
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Text(text = stringResource(id = R.string.settelmentButton))
                }
            }
        }
    }
}
