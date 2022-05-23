package com.example.e.main.group

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.e.R
import com.example.e.domain.AccountingGroup
import com.example.e.domain.GroupId
import com.example.e.sampleGroups
import com.example.e.ui.theme.ETheme
import kotlinx.coroutines.launch

@Composable
fun GroupPicker(
    groupCardData: GroupCardData,
    addNewGroup: () -> Unit,
    setCurrentGroup: (AccountingGroup) -> Unit
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit, key2 = groupCardData, block = {
        coroutineScope.launch {
            groupCardData.currentGroupIndex().let {
                if (it > -1) {
                    listState.animateScrollToItem(it)
                }
            }
        }
    })

    LazyRow(
        state = listState,
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {
        itemsIndexed(groupCardData.groups) { index, item ->
            GroupCard(
                group = item,
                isCurrentGroup = item.id == groupCardData.currentGroupId,
                setCurrentGroup = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(index)
                    }
                    setCurrentGroup(item)
                }
            )

            Box(modifier = Modifier.width(IntrinsicSize.Max)) {
                Spacer(modifier = Modifier.width(2.dp))
                Divider(color = MaterialTheme.colors.surface, modifier = Modifier.fillMaxWidth())
            }
        }
        item() {
            AddGroupCard(addNewGroup = addNewGroup)
        }
    }
}

@Composable
fun GroupCard(
    group: AccountingGroup,
    isCurrentGroup: Boolean,
    setCurrentGroup: (AccountingGroup) -> Unit
) {
    val currentGroupColor = if (isCurrentGroup) {
        MaterialTheme.colors.surface
    } else {
        MaterialTheme.colors.background
    }
    val currentTextColor = if (isCurrentGroup) {
        MaterialTheme.colors.onSurface
    } else {
        MaterialTheme.colors.onBackground
    }
    val shape = RoundedCornerShape(
        topStart = 6.dp, topEnd = 6.dp, bottomEnd = 0.dp, bottomStart = 0.dp
    )
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .width(IntrinsicSize.Max)
            .clickable {
                setCurrentGroup(group)
            }
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .clip(shape = shape)
                .border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colors.surface,
                    ),
                    shape = shape
                )
                .background(currentGroupColor)
                .padding(8.dp)
        ) {
            Text(text = group.title, color = currentTextColor)
        }
        Spacer(modifier = Modifier.width(2.dp))
        Divider(color = currentGroupColor, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun AddGroupCard(addNewGroup: () -> Unit) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .clickable { addNewGroup() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 5.dp, topEnd = 5.dp, bottomEnd = 0.dp, bottomStart = 0.dp
                    )
                )
                .background(MaterialTheme.colors.background)
                .padding(4.dp)

        ) {
            Row {
                Icon(Icons.Filled.Add, stringResource(id = R.string.addNewGroup))
                Text(
                    text = stringResource(id = R.string.addNewGroup),
                    color = MaterialTheme.colors.onBackground
                )
            }
        }
        Divider(color = MaterialTheme.colors.surface, modifier = Modifier.fillMaxWidth())
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
fun GroupPreview() {
    ETheme {
        GroupPicker(
            GroupCardData(sampleGroups, GroupId(2)),
            {}, {}
        )
    }
}
