package com.example.e.addexpense

import androidx.lifecycle.MutableLiveData
import com.example.e.addexpense.model.AddExpenseEffect
import com.example.e.addexpense.model.NewExpenseInput
import com.example.e.addexpense.participantpicker.ParticpantCardState
import com.example.e.data.repository.RepositoryImpl
import com.example.e.domain.Expense
import com.example.e.domain.User.Companion.toParticipantCardState
import com.example.e.logThrowable
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@ExperimentalSerializationApi
class AddExpenseUseCase @Inject constructor(
    private val repositoryImpl: RepositoryImpl,
    val preferences: com.example.e.sharedpreferences.Preferences
) {
    suspend fun participants(

        borrowersState: MutableLiveData<List<ParticpantCardState>>,
        payersState: MutableLiveData<List<ParticpantCardState>>,
    ) = repositoryImpl.groupUsers(preferences.groupId()).map {
        it.toParticipantCardState(SELECTED_BY_DEFAULT).also {
            borrowersState.value = it
            payersState.value = it
        }
    }

    suspend fun addExpense(
        input: NewExpenseInput,
        addExpenseEffect: MutableLiveData<AddExpenseEffect?>
    ) = try {
        addExpenseEffect.value = AddExpenseEffect.ShowProgressBar
        repositoryImpl.addExpense(input.toExpense(), preferences.groupId())
        addExpenseEffect.value = AddExpenseEffect.GoToSplitScreen(input)
    } catch (e: Throwable) {
        e.logThrowable(this)
        addExpenseEffect.value = AddExpenseEffect.ShowGenericErrorMessage(e)
    }

    suspend fun addExpense(input: Expense, addExpenseEffect: MutableLiveData<AddExpenseEffect?>) =
        try {
            addExpenseEffect.value = AddExpenseEffect.ShowProgressBar
            repositoryImpl.addExpense(input, preferences.groupId())
            addExpenseEffect.value = AddExpenseEffect.GoToSplitScreen()
        } catch (e: Throwable) {
            e.logThrowable(this)
            addExpenseEffect.value = AddExpenseEffect.ShowGenericErrorMessage(e)
        }


    companion object {
        private const val SELECTED_BY_DEFAULT = false
    }
}
