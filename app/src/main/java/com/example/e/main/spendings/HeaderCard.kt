package com.example.e.main.spendings

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.e.*
import com.example.e.domain.AccountingGroup
import com.example.e.main.MainScreenNavigationContract
import com.example.e.ui.theme.ETheme

@Composable
fun HeaderCard(
    currentSpendingState: CurrentSpendingState,
    navigation: MainScreenNavigationContract,
    setCurrentGroup: (AccountingGroup) -> Unit
) {
    Surface(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        when (currentSpendingState) {
            is CurrentSpendingState.Success -> {
                HeaderCardContent(
                    headerCardData = currentSpendingState.headerCardData,
                    navigation = navigation,
                    setCurrentGroup = setCurrentGroup
                )
            }
            is CurrentSpendingState.Loading -> {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    DotsPulsing()
                }
            }
            is CurrentSpendingState.Error -> ErrorHeader()
        }
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
fun DefaultPreview() {
    ETheme {
        HeaderCard(
            CurrentSpendingState.Success(sampleConversation),
            mainScreenNavigationContract()
        ) {}
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Loading Dark Mode"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    name = "Loading Light Mode"
)
@Composable
fun LoadingPreview() {
    ETheme {
        HeaderCard(
            CurrentSpendingState.Loading,
            mainScreenNavigationContract()
        ) {}
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Error Dark Mode"
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true,
    name = "Error Light Mode"
)
@Composable
fun ErrorPreview() {
    ETheme {
        HeaderCard(
            CurrentSpendingState.Error,
            mainScreenNavigationContract()
        ) {}
    }
}

@Composable
private fun mainScreenNavigationContract() = object : MainScreenNavigationContract {
    override fun goToAddExpense() {}
    override fun menuAction() {}
    override fun goToSettlement() {}
    override fun goToAddNewGroup() {}
}
