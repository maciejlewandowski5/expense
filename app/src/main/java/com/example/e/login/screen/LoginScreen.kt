package com.example.e.login.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.e.InputWrapperCard
import com.example.e.R
import com.example.e.source.SwitchSourceCard
import com.example.e.source.SwitchSourceCardContract
import kotlinx.serialization.ExperimentalSerializationApi

@Composable
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalSerializationApi
fun LoginScreen(viewModel: LoginViewModel, navigateHome: () -> Unit) {
    val userName by viewModel.userName.observeAsState(initial = "")
    val userPassword by viewModel.password.observeAsState(initial = "")
    val loginEffect by viewModel.loginEffect.observeAsState()

    if (loginEffect is LoginEffect.GoToMainScreen) LaunchedEffect(
        key1 = Unit,
        block = { navigateHome() }
    )
    LoginScaffold(
        userName = userName,
        userPassword = userPassword,
        loginEffect = loginEffect,
        loginClick = viewModel::loginClick,
        setUserName = viewModel::setUserName,
        setUserPassword = viewModel::setPassword,
        switchSourceContract = viewModel
    )
}

@Composable
@ExperimentalMaterialApi
@ExperimentalFoundationApi
fun LoginScaffold(
    userName: String,
    userPassword: String,
    loginEffect: LoginEffect?,
    loginClick: () -> Unit,
    setUserName: (String) -> Unit,
    setUserPassword: (String) -> Unit,
    switchSourceContract: SwitchSourceCardContract
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(loginClick) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.addNewExpense)
                    )
                }
            },
            content = {
                LoginContent(
                    userName = userName,
                    userPassword = userPassword,
                    loginEffect = loginEffect,
                    setUserName = setUserName,
                    setUserPassword = setUserPassword,
                    switchSourceContract = switchSourceContract
                )
            }
        )
    }
}

@Composable
@ExperimentalMaterialApi
@ExperimentalFoundationApi
fun LoginContent(
    userName: String,
    userPassword: String,
    loginEffect: LoginEffect?,
    setUserName: (String) -> Unit,
    setUserPassword: (String) -> Unit,
    switchSourceContract: SwitchSourceCardContract
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
            SwitchSourceCard(switchSourceContract)
        }
        val (errorMessage: String?, isLoading) = when (loginEffect) {
            is LoginEffect.LoginInProgress -> Pair(null, true)
            is LoginEffect.ShowErrorMessage -> Pair(
                stringResource(id = R.string.wrongCreditentials),
                false
            )
            else -> Pair(null, false)
        }
        InputWrapperCard(
            errorMessage = errorMessage,
            loadingBar = isLoading,
            content = {
                LoginInputs(
                    userName = userName,
                    userPassword = userPassword,
                    setUserName = setUserName,
                    setUserPassword = setUserPassword
                )
            }
        )
    }
}

@Composable
@ExperimentalMaterialApi
@ExperimentalFoundationApi
fun LoginInputs(
    userName: String,
    userPassword: String,
    setUserName: (String) -> Unit,
    setUserPassword: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = userName,
            onValueChange = setUserName,
            label = { Text(text = stringResource(id = R.string.userName)) }
        )
        TextField(
            value = userPassword,
            onValueChange = setUserPassword,
            label = { Text(text = stringResource(id = R.string.password)) }
        )
    }
}
