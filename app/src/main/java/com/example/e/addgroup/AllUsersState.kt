package com.example.e.addgroup

import com.example.e.domain.User

sealed class AllUsersState() {
    object Loading : AllUsersState()
    class Success(val users: List<User>) : AllUsersState()
    class Error(val error: Throwable) : AllUsersState()
}
