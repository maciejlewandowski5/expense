package com.example.e.addexpense

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e.addexpense.model.AddExpenseEffect
import com.example.e.addexpense.model.InputErrorTypes
import com.example.e.addexpense.model.NewExpenseInput
import com.example.e.addexpense.participantpicker.ParticpantCardState
import com.example.e.addexpense.view.AddExpenseContentContract
import com.example.e.logThrowable
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.NumberFormatException
import java.math.BigDecimal
import java.time.Clock
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val addExpenseUseCase: AddExpenseUseCase,
    clock: Clock
) : ViewModel(), AddExpenseContentContract {

    private val _borrowersState: MutableLiveData<List<ParticpantCardState>> = MutableLiveData(
        emptyList()
    )
    val borrowersState: LiveData<List<ParticpantCardState>> = _borrowersState

    private val _payersState: MutableLiveData<List<ParticpantCardState>> = MutableLiveData(
        emptyList()
    )
    val payersState: LiveData<List<ParticpantCardState>> = _payersState

    private val _expenseDateTime: MutableLiveData<LocalDateTime> =
        MutableLiveData(LocalDateTime.now(clock))
    val expenseDateTime: LiveData<LocalDateTime> = _expenseDateTime

    private val _title: MutableLiveData<String> =
        MutableLiveData("")
    val title: LiveData<String> = _title

    private val _isExternal: MutableLiveData<Boolean> = MutableLiveData(true)
    val isExternal: LiveData<Boolean> = _isExternal

    private val _newExpenseInput: MutableLiveData<NewExpenseInput> = MutableLiveData(null)
    val newExpenseInput: LiveData<NewExpenseInput> = _newExpenseInput

    private val _addExpenseEffect: MutableLiveData<AddExpenseEffect?> = MutableLiveData(null)
    val addExpenseEffect: LiveData<AddExpenseEffect?> = _addExpenseEffect

    override fun borrowerClick(participantState: ParticpantCardState) = _borrowersState.value?.let {
        _borrowersState.value = it.switchSelection(it.getParticipant(participantState))
    }

    override fun payerClick(participantState: ParticpantCardState) = _payersState.value?.let {
        _payersState.value = it.switchSelection(it.getParticipant(participantState))
    }

    override fun externalChanged(isExternal: Boolean) {
        _isExternal.value = isExternal
        Log.d(this::class.simpleName, "isExternal set to: $isExternal")
    }

    override fun setTitle(title: String) = _borrowersState.value?.let { _title.value = title }

    fun setDate(date: LocalDateTime) = _borrowersState.value?.let { _expenseDateTime.value = date }

    fun loadParticipants() = viewModelScope.launch {
        addExpenseUseCase.participants(_borrowersState, _payersState)
    }

    fun addExpenseClick() = viewModelScope.launch {
        if (userInputsHasNotNulls() && userInputsInNotEmpty()
        ) {
            tryAddExpense()
        } else {
            _addExpenseEffect.value =
                AddExpenseEffect.ShowInputErrorMessage(InputErrorTypes.ALL)
        }
    }

    private fun List<ParticpantCardState>.getParticipant(
        participantState: ParticpantCardState
    ) = first { it.user.id == participantState.user.id }

    private fun List<ParticpantCardState>.switchSelection(
        participant: ParticpantCardState
    ) = toMutableList().also {
        it.add(
            it.indexOf(participant),
            participant.copy(isSelected = !participant.isSelected)
        )
        it.remove(participant)
    }

    private fun tryAddExpense() = try {
        val ne = newExpenseInput(BigDecimal.ZERO)
        _newExpenseInput.value = ne
        _addExpenseEffect.value = AddExpenseEffect.GoToSplitScreen(ne)
    } catch (e: NumberFormatException) {
        e.logThrowable(this)
        _addExpenseEffect.value =
            AddExpenseEffect.ShowInputErrorMessage(InputErrorTypes.AMOUNT)
    }

    private fun newExpenseInput(amount: BigDecimal) = NewExpenseInput(
        _borrowersState.value!!,
        _payersState.value!!,
        amount,
        title.value!!,
        expenseDateTime.value!!,
        isExternal.value!!
    )

    private fun userInputsHasNotNulls() = _borrowersState.value != null &&
        _payersState.value != null &&
        title.value != null &&
        expenseDateTime.value != null

    private fun userInputsInNotEmpty() = _borrowersState.value!!.any { it.isSelected } &&
        _payersState.value!!.any { it.isSelected } &&
        title.value!!.isNotEmpty()
}
