package com.example.e.addgroup

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.e.DEFAULT_AUTOGENERATE_GROUP_ID
import com.example.e.data.Repository
import com.example.e.domain.AccountingGroup
import com.example.e.domain.User
import com.example.e.sharedpreferences.Preferences
import javax.inject.Inject

class AddGroupUseCase @Inject constructor(
    private val repository: Repository,
    private val preferences: Preferences
) {
    suspend fun addUser(userName: String) =
        User(id = userName, userName).also { repository.addUser(it) }

    suspend fun fetchUsers(allUsersState: MutableLiveData<AllUsersState>) = try {
        allUsersState.value = AllUsersState.Loading
        allUsersState.value = AllUsersState.Success(repository.allUsers())
    } catch (e: Throwable) {
        e.message?.let { Log.e(this::class.simpleName, it) }
        allUsersState.value = AllUsersState.Error(e)
    }

    suspend fun addGroup(name: String, members: List<User>) = repository
        .addGroup(AccountingGroup(DEFAULT_AUTOGENERATE_GROUP_ID, name), members)
        .also { preferences.setGroupId(it) }
}
