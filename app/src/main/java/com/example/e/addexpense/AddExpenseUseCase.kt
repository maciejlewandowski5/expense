package com.example.e.addexpense

import androidx.lifecycle.MutableLiveData
import com.example.e.addexpense.model.NewExpenseInput
import com.example.e.addexpense.participantpicker.ParticpantCardState
import com.example.e.data.repository.RepositoryImpl
import com.example.e.domain.GroupId
import com.example.e.domain.User.Companion.toParticipantCardState
import com.example.e.logThrowable
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@ExperimentalSerializationApi
class AddExpenseUseCase @Inject constructor(private val repositoryImpl: RepositoryImpl) {
    suspend fun participants(
        groupId: GroupId,
        borrowersState: MutableLiveData<List<ParticpantCardState>>,
        payersState: MutableLiveData<List<ParticpantCardState>>,
    ) = repositoryImpl.groupUsers(groupId).map {
        it.toParticipantCardState(SELECTED_BY_DEFAULT).also {
            borrowersState.value = it
            payersState.value = it
        }
    }

    suspend fun addExpense(
        input: NewExpenseInput,
        groupId: GroupId,
        addExpenseEffect: MutableLiveData<AddExpenseEffect?>
    ) = try {
        addExpenseEffect.value = AddExpenseEffect.ShowProgressBar
        repositoryImpl.addExpense(input.toExpense(), groupId)
        addExpenseEffect.value = AddExpenseEffect.GoToMainScreen
    } catch (e: Throwable) {
        e.logThrowable(this)
        addExpenseEffect.value = AddExpenseEffect.ShowGenericErrorMessage(e)
    }

    companion object {
        private const val SELECTED_BY_DEFAULT = false
    }
}
