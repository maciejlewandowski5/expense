package com.example.e.login.session

import com.example.e.modules.NetworkModule
import com.example.e.login.remote.LoginRequest
import com.example.e.login.remote.LoginResponse
import com.example.e.login.remote.RefreshRequest
import kotlinx.serialization.ExperimentalSerializationApi
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

@ExperimentalSerializationApi
interface AuthService {
    @POST(NetworkModule.LOGIN_URL)
    suspend fun login(@Body loginRequestBody: LoginRequest): Response<LoginResponse>

    @POST(NetworkModule.REFRESH_URL)
    suspend fun refreshToken(@Body refreshRequestBody: RefreshRequest): Response<LoginResponse>
}
