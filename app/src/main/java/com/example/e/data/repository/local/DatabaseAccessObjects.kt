package com.example.e.data.repository.local

import androidx.room.*
import com.example.e.data.*
import com.example.e.domain.AccountingGroup
import com.example.e.domain.Participant
import com.example.e.domain.User

@Dao
abstract class ExpenseDao {

    @Transaction
    open suspend fun insertExpenseAndParticipants(
        expenseModel: ExpenseModel,
        participants: List<Participant>
    ): Long {
        val id = insert(expenseModel)
        participants.forEach { insertParticipant(it.toParticipantModel(id)) }
        return id
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(expenseModel: ExpenseModel): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertParticipant(participantModel: ParticipantModel)
}

@Dao
abstract class GroupDao {

    @Transaction
    open suspend fun addGroupWithMembers(
        group: AccountingGroup,
        members: List<User>
    ): Long {
        val id = insert(group.toAccountingGroupModel())
        members.forEach { insertMember(it.toMemberModel(id)) }
        return id
    }

    @Transaction
    @Query("SELECT * FROM groups_table WHERE id = :groupId LIMIT 1")
    abstract suspend fun groupWithExpenses(groupId: Long): AccountingGroupWithExpensesModel?

    @Query("SELECT * FROM groups_table")
    abstract suspend fun groups(): List<AccountingGroupModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(groupModel: AccountingGroupModel): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertMember(memberModel: MemberModel)
}

@Dao
interface UserDao {
    @Query(
        """
            SELECT z.userId as id,users_table.name FROM (
                SELECT userId FROM members_table WHERE accountingGroupId = :groupId) AS z 
                JOIN users_table      
                ON z.userId = users_table.id
        """
    )
    suspend fun groupUsers(groupId: Long): List<UserModel>

    @Query("SELECT * FROM users_table ORDER BY name")
    suspend fun users(): List<UserModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userModel: UserModel): Long
}

@Dao
interface MemberDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(memberModel: MemberModel): Long
}

@Dao
interface ParticipantDao {
    @Query("SELECT * FROM participants_table WHERE expenseId = :expenseId")
    suspend fun participants(expenseId: Long): List<ParticipantModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(participantModel: ParticipantModel)
}
