package com.example.e.addgroup

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.e.DEFAULT_AUTOGENERATE_GROUP_ID
import com.example.e.data.repository.RepositoryImpl
import com.example.e.domain.AccountingGroup
import com.example.e.domain.User
import com.example.e.sharedpreferences.Preferences
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@ExperimentalSerializationApi
class AddGroupUseCase @Inject constructor(
    private val repository: RepositoryImpl,
    private val preferences: Preferences
) {
    suspend fun addUser(userName: String) =
        User(id = userName, userName).also { repository.addUser(it) }

    suspend fun fetchUsers(allUsersState: MutableLiveData<AllUsersState>) {
        allUsersState.value = AllUsersState.Loading
        repository.allUsers()
            .fold({
                it.message?.let { Log.e(this::class.simpleName, it) }
                allUsersState.value = AllUsersState.Error(it)
            }, { allUsersState.value = AllUsersState.Success(it) })
    }

    suspend fun addGroup(name: String, members: List<User>) = repository
        .addGroup(AccountingGroup(DEFAULT_AUTOGENERATE_GROUP_ID, name), members)
        .tap { preferences.setGroupId(it) }
}
