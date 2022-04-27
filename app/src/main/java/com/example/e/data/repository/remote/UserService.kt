package com.example.e.data.repository.remote

import com.example.e.modules.NetworkModule.Companion.ADD_USER
import com.example.e.modules.NetworkModule.Companion.ALL_USERS
import com.example.e.modules.NetworkModule.Companion.GROUP_ID
import com.example.e.modules.NetworkModule.Companion.GROUP_USERS
import com.example.e.data.remotemodels.AddUserRequest
import com.example.e.data.remotemodels.UserResponse
import kotlinx.serialization.ExperimentalSerializationApi
import retrofit2.Response
import retrofit2.http.*

@ExperimentalSerializationApi
interface UserService {
    @GET(GROUP_USERS)
    suspend fun groupUsers(@Path(GROUP_ID) groupId: Long): Response<List<UserResponse>>

    @POST(ADD_USER)
    suspend fun addUser(@Body addUserRequest: AddUserRequest): Response<Long>

    @GET(ALL_USERS)
    suspend fun allUsers(): Response<List<UserResponse>>
}
