package com.example.e.addgroup.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.e.InputWrapperCard
import com.example.e.R
import com.example.e.addgroup.AddGroupEffect
import com.example.e.addgroup.AddGroupViewModel
import com.example.e.addgroup.AllUsersState
import com.example.e.domain.User

@Composable
@ExperimentalMaterialApi
@ExperimentalFoundationApi
fun AddGroupScreen(viewModel: AddGroupViewModel, goToMainScreen: () -> Unit) {
    val groupName by viewModel.groupName.observeAsState("")
    val userNameInput by viewModel.userNameInput.observeAsState("")
    val pickerUsers by viewModel.pickedParticipants.observeAsState(emptyList())
    val allUsersState by viewModel.allUsersState.observeAsState(AllUsersState.Loading)
    val addGroupEffect by viewModel.addGroupEffect.observeAsState()

    if (addGroupEffect is AddGroupEffect.GoToMainScreen) {
        LaunchedEffect(key1 = Unit, block = { goToMainScreen() })
    }

    AddGroupScaffold(
        groupName = groupName,
        userNameInput = userNameInput,
        pickedUsers = pickerUsers,
        allUsersState = allUsersState,
        contract = viewModel,
        addGroupEffect = addGroupEffect
    )
}

@Composable
@ExperimentalMaterialApi
@ExperimentalFoundationApi
fun AddGroupScaffold(
    groupName: String,
    userNameInput: String,
    pickedUsers: List<User>,
    allUsersState: AllUsersState,
    contract: AddGroupContentContract,
    addGroupEffect: AddGroupEffect?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = contract::addGroup) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.addNewExpense)
                    )
                }
            },
            content = {
                AddGroupContent(
                    groupName = groupName,
                    userNameInput = userNameInput,
                    pickedUsers = pickedUsers,
                    allUsersState = allUsersState,
                    contract = contract,
                    addGroupEffect = addGroupEffect
                )
            }
        )
    }
}

@Composable
@ExperimentalMaterialApi
@ExperimentalFoundationApi
fun AddGroupContent(
    groupName: String,
    userNameInput: String,
    pickedUsers: List<User>,
    allUsersState: AllUsersState,
    contract: AddGroupContentContract,
    addGroupEffect: AddGroupEffect?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        val (errorMessage: String?, isLoading, users) = when (allUsersState) {
            is AllUsersState.Loading -> Triple(null, true, emptyList())
            is AllUsersState.Success -> resolveAddGroupEffect(addGroupEffect, allUsersState)
            is AllUsersState.Error -> Triple(
                stringResource(id = R.string.genericError), false,
                emptyList()
            )
        }
        InputWrapperCard(
            errorMessage = errorMessage,
            loadingBar = isLoading,
            content = {
                AddGroupInputs(
                    groupName = groupName,
                    userNameInput = userNameInput,
                    pickedUsers = pickedUsers,
                    allUsers = users,
                    contract = contract
                )
            }
        )
    }
}

@Composable
private fun resolveAddGroupEffect(
    addGroupEffect: AddGroupEffect?,
    allUsersState: AllUsersState.Success
): Triple<String?, Boolean, List<User>> {
    val isLoading = addGroupEffect is AddGroupEffect.ShowLoadingIndicator
    val errorMessage = resolveShowErrorMessage(addGroupEffect)
    return Triple(errorMessage, isLoading, allUsersState.users)
}

@Composable
private fun resolveShowErrorMessage(addGroupEffect: AddGroupEffect?) =
    if (addGroupEffect is AddGroupEffect.ShowErrorMessage) {
        stringResource(id = R.string.genericError)
    } else {
        null
    }
