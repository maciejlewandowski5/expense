package com.example.e.addgroup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e.addgroup.view.AddGroupContentContract
import com.example.e.domain.User
import com.example.e.logThrowable
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddGroupViewModel @Inject constructor(
    private val analytics: FirebaseAnalytics,
    private val addGroupUseCase: AddGroupUseCase
) :
    ViewModel(), AddGroupContentContract {

    private val _groupName: MutableLiveData<String> = MutableLiveData("")
    val groupName: LiveData<String> = _groupName

    private val _userNameInput: MutableLiveData<String> = MutableLiveData("")
    val userNameInput: LiveData<String> = _userNameInput

    private val _pickedParticipants: MutableLiveData<List<User>> = MutableLiveData(emptyList())
    val pickedParticipants: LiveData<List<User>> = _pickedParticipants

    private val _allUsersState: MutableLiveData<AllUsersState> =
        MutableLiveData(AllUsersState.Loading)
    val allUsersState: LiveData<AllUsersState> = _allUsersState

    private val _addGroupEffect: MutableLiveData<AddGroupEffect?> =
        MutableLiveData(null)
    val addGroupEffect: LiveData<AddGroupEffect?> = _addGroupEffect

    override fun unpickUser(unselectedUser: User) {
        _pickedParticipants.value = _pickedParticipants.value?.toMutableList()?.also {
            it.remove(unselectedUser)
        }
        if (_allUsersState.value is AllUsersState.Success) {
            _allUsersState.value =
                (_allUsersState.value as AllUsersState.Success).users.toMutableList().also {
                    it.add(unselectedUser)
                }.toList().let {
                    AllUsersState.Success(
                        it
                    )
                }
        }
    }

    override fun pickUser(pickedUser: User) {
        _pickedParticipants.value = _pickedParticipants.value?.toMutableList()?.also {
            it.add(pickedUser)
        }
        if (_allUsersState.value is AllUsersState.Success) {
            _allUsersState.value =
                (_allUsersState.value as AllUsersState.Success).users.toMutableList().also {
                    it.remove(pickedUser)
                }.toList().let {
                    AllUsersState.Success(
                        it
                    )
                }
        }
    }

    override fun addAndPickNewUser() = _userNameInput.value?.let { userName ->
        if (userName.isNotBlank()) addAndPickNewUser(userName)
    }

    private fun addAndPickNewUser(userName: String) = viewModelScope.launch {
        _pickedParticipants.value = _pickedParticipants.value?.toMutableList()?.also {
            if (it.firstOrNull { it.id == userName } == null &&
                _allUsersState.value is AllUsersState.Success && (_allUsersState.value as AllUsersState.Success).users.firstOrNull { it.id == userName } == null
            ) {
                it.add(addGroupUseCase.addUser(userName))
            }

            _userNameInput.value = ""
        }
    }

    override fun setGroupName(newGroupName: String) {
        _groupName.value = newGroupName
    }

    override fun setUserNameInput(newUserName: String) {
        _userNameInput.value = newUserName
    }

    override fun addGroup() {
        _addGroupEffect.value = AddGroupEffect.ShowLoadingIndicator
        viewModelScope.launch {
            if (_groupName.value != null && _pickedParticipants.value != null && _pickedParticipants.value!!.isNotEmpty()) {
                try {
                    addGroupUseCase.addGroup(_groupName.value!!, _pickedParticipants.value!!)
                    analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
                        param(FirebaseAnalytics.Param.ITEM_NAME, _groupName.value!!)
                    }
                    _addGroupEffect.value = AddGroupEffect.GoToMainScreen
                } catch (e: Throwable) {
                    e.logThrowable(this)
                    _addGroupEffect.value = AddGroupEffect.ShowErrorMessage
                }
            }
        }
    }

    fun fetchUsers() {
        viewModelScope.launch {
            addGroupUseCase.fetchUsers(_allUsersState)
        }
    }
}
