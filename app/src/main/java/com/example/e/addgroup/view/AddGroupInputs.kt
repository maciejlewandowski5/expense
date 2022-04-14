package com.example.e.addgroup.view

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.e.R
import com.example.e.domain.User
import com.example.e.sampleUsers
import com.example.e.ui.theme.ETheme

@Composable
@ExperimentalMaterialApi
@ExperimentalFoundationApi
fun AddGroupInputs(
    groupName: String,
    userNameInput: String,
    pickedUsers: List<User>,
    allUsers: List<User>,
    contract: AddGroupContentContract
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = groupName,
            onValueChange = { contract.setGroupName(it) },
            label = { Text(text = stringResource(id = R.string.groupName)) }
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.users),
            color = MaterialTheme.colors.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        MemberPicker(userNameInput, contract, allUsers)
        PickedMembersCard(pickedUsers, contract)
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
@ExperimentalMaterialApi
@ExperimentalFoundationApi
fun AddGroupPreview() {
    ETheme {
        AddGroupInputs(
            "", "", sampleUsers, sampleUsers,
            contract =
            object : AddGroupContentContract {
                override fun unpickUser(unselectedUser: User) {}
                override fun pickUser(pickedUser: User) {}
                override fun setGroupName(newGroupName: String) {}
                override fun setUserNameInput(newUserName: String) {}
                override fun addAndPickNewUser() {}
                override fun addGroup() {}
            }
        )
    }
}
