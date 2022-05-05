package com.example.e.data.repository.remote

import com.example.e.data.remotemodels.*
import com.example.e.modules.NetworkModule
import com.example.e.modules.NetworkModule.Companion.EXPENSE_ID
import com.example.e.modules.NetworkModule.Companion.GROUP_ID
import kotlinx.serialization.ExperimentalSerializationApi
import retrofit2.Response
import retrofit2.http.*

@ExperimentalSerializationApi
interface GroupService {
    @GET(NetworkModule.GROUPS_URL)
    suspend fun allGroups(): Response<AllGroupsResponse>

    @GET(NetworkModule.GROUP_DETAILS_URL)
    suspend fun groupDetails(@Path(GROUP_ID) groupId: Long): Response<GroupDetailsResponse>

    @POST(NetworkModule.ADD_GROUP_URL)
    suspend fun addGroup(@Body addGroupRequest: AddGroupRequest): Response<Long>

    @POST(NetworkModule.ADD_MEMBER)
    suspend fun addMember(
        @Body userRequest: UserRequest,
        @Path(GROUP_ID) groupId: Long
    ): Response<Long>

    @POST(NetworkModule.ADD_EXPENSE)
    suspend fun addExpense(
        @Body expenseRequest: ExpenseRequest,
        @Path(GROUP_ID) groupId: Long
    ): Response<Long>

    @DELETE(NetworkModule.DELETE_EXPENSES)
    fun deleteExpense(@Path(EXPENSE_ID) expenseId: Long): Response<Long>
}
