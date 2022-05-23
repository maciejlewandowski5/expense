package com.example.e.main.spendings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.e.R
import com.example.e.main.MainScreenNavigationContract
import com.example.e.main.MainViewContract
import com.example.e.main.group.GroupPicker
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

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

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun HeaderCardContent(
    headerCardData: HeaderCardData,
    navigation: MainScreenNavigationContract,
    contract: MainViewContract,
    isRefreshing: Boolean
) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.distinctUntilChanged().collect() {
            contract.setCurrentGroup(it)
        }
    }
    LaunchedEffect(key1 = Unit, key2 = headerCardData, block = {
        coroutineScope.launch {
            pagerState.scrollToPage(contract.getCurrentGroupIndex())
        }
    })
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
        HorizontalPager(
            count = headerCardData.groupCardData.groups.size,
            state = pagerState
        ) { page ->
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .padding(horizontal = 16.dp)
            ) {

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
                Row(
                    modifier = Modifier
                        .align(alignment = Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = when {
                            page == contract.getCurrentGroupIndex() -> headerCardData.formattedAmount()
                            else -> stringResource(id = R.string.zeroPLN)
                        },
                        style = MaterialTheme.typography.h3,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                    )
                }
                if (headerCardData.groupCardData.groups.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .align(alignment = Alignment.End)
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
    }
}
