package com.example.e.addgroup.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import com.example.e.R
import com.example.e.addexpense.participantpicker.ParticpantCardState
import com.example.e.domain.User
import com.example.e.participant.ParticipantCard

@ExperimentalMaterialApi
@Composable
internal fun MemberPicker(
    userNameInput: String,
    contract: AddGroupContentContract,
    allUsers: List<User>
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = userNameInput,
            onValueChange = { contract.setUserNameInput(it) },
            label = { Text(text = stringResource(id = R.string.addUser)) },
            trailingIcon = {
                Row {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                    IconButton(
                        onClick = { contract.addAndPickNewUser() },
                        modifier = Modifier.clearAndSetSemantics { }
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            stringResource(id = R.string.addUser),
                        )
                    }
                }
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            allUsers.forEach { selectedUser ->
                DropdownMenuItem(
                    onClick = {
                        contract.pickUser(selectedUser)
                        expanded = false
                    }
                ) {
                    Text(text = selectedUser.name)
                }
            }
        }
    }
}

@Composable
@ExperimentalFoundationApi
internal fun PickedMembersCard(
    pickedUsers: List<User>,
    contract: AddGroupContentContract
) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(100.dp), content = {
            this.items(count = pickedUsers.size, itemContent = {
                val user = pickedUsers[it]
                ParticipantCard(
                    state = ParticpantCardState(user = user, isSelected = true),
                    participantClick = {

                        contract.unpickUser(user)
                    },
                    tileColor = MaterialTheme.colors.primary,
                    textColor = MaterialTheme.colors.onPrimary
                )
            })
        }
    )
}
