package com.example.e.modules

import com.example.e.data.repository.remote.GroupService
import com.example.e.data.repository.remote.UserService
import com.example.e.login.session.AuthInterceptor
import com.example.e.login.session.AuthService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Authenticator
import okhttp3.EventListener
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@ExperimentalSerializationApi
class NetworkModule {

    @Singleton
    @Provides
    fun okHttpClient(authInterceptor: AuthInterceptor) =
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().also { it.setLevel(HttpLoggingInterceptor.Level.BODY) })
            .addInterceptor(authInterceptor)
            .eventListener(object : EventListener() {})
            .build()

    @Singleton
    @Provides
    fun provideRestClient(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder().client(okHttpClient).baseUrl(BASE_URL)
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType())).build()

    @Singleton
    @Provides
    fun provideLoginService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Singleton
    @Provides
    fun provideGroupsService(retrofit: Retrofit): GroupService =
        retrofit.create(GroupService::class.java)

    @Singleton
    @Provides
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    companion object {
        const val GROUP_ID = "groupId"
        const val EXPENSE_ID = "expenseId"

        const val BASE_URL = "https://c8f57ac9-ddae-4ed6-8068-565a91fcdaf7.mock.pstmn.io"

        const val LOGIN_URL = "auth/login"
        const val REFRESH_URL = "auth/refresh"
        const val GROUPS_URL = "s/groups"
        const val GROUP_DETAILS_URL = "s/group/{$GROUP_ID}"
        const val ADD_GROUP_URL = "s/group"
        const val GROUP_USERS = "s/group/{$GROUP_ID}/users"
        const val ADD_USER = "s/user"
        const val ALL_USERS = "s/users"
        const val ADD_MEMBER = "s/group/{$GROUP_ID}/member"
        const val ADD_EXPENSE = "s/group/{$GROUP_ID}/expense"
        const val DELETE_EXPENSES = "/s/expenses/{$EXPENSE_ID}"
    }
}
