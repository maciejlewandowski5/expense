package com.example.e.addgroup.view

import com.example.e.domain.User

interface AddGroupContentContract {
    fun unpickUser(unselectedUser: User): Unit?
    fun pickUser(pickedUser: User): Unit?
    fun setGroupName(newGroupName: String): Unit?
    fun setUserNameInput(newUserName: String): Unit?
    fun addAndPickNewUser(): Unit?
    fun addGroup(): Unit?
}
