package com.example.e.split

import androidx.lifecycle.*
import com.example.e.addexpense.AddExpenseUseCase
import com.example.e.addexpense.model.AddExpenseEffect
import com.example.e.addexpense.model.NewExpenseInput
import com.example.e.addexpense.participantpicker.ParticpantCardState.Companion.toCustomSplitParticipants
import com.example.e.changeSign
import com.example.e.domain.Participant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class SplitViewModel @Inject constructor(private val createExpenseUseCase: AddExpenseUseCase) :
    ViewModel() {
    private val _amountToSplit: MutableLiveData<String?> =
        MutableLiveData(BigDecimal.ZERO.toPlainString())
    val amountToSplit: LiveData<String?> = _amountToSplit

    private val _totalExpenseAmount: MutableLiveData<String> =
        MutableLiveData(BigDecimal.ZERO.toPlainString())
    val totalExpenseAmount: LiveData<String> = _totalExpenseAmount

    private val _errorMessage: MutableLiveData<String?> = MutableLiveData(null)
    val errorMessage: LiveData<String?> = _errorMessage

    private val _borrowersState: MutableLiveData<List<CustomSplitParticipant>> = MutableLiveData(
        emptyList()
    )
    val borrowersState: LiveData<List<CustomSplitParticipant>> = _borrowersState

    private val _payersState: MutableLiveData<List<CustomSplitParticipant>> = MutableLiveData(
        emptyList()
    )
    val payersState: LiveData<List<CustomSplitParticipant>> = _payersState

    private val _addExpenseEffect: MutableLiveData<AddExpenseEffect?> = MutableLiveData(null)
    val addExpenseEffect: LiveData<AddExpenseEffect?> = _addExpenseEffect

    var newExpense: NewExpenseInput? = null
        set(value) {
            if (value != null) {
                _borrowersState.value =
                    value.borrowers.toCustomSplitParticipants(value.amount.abs())
                _payersState.value = value.payers.toCustomSplitParticipants(value.amount.abs())
                val amountPayed =
                    _payersState.value?.sumOf { it.participant.amount } ?: BigDecimal.ZERO
                val amountBorrowed =
                    _borrowersState.value?.sumOf { it.participant.amount } ?: BigDecimal.ZERO
                val expectedAmount = value.amount

                _amountToSplit.value = when {
                    _borrowersState.value?.size == 1 -> {
                        expectedAmount.minus(amountPayed).abs().changeSign().toPlainString()
                    }
                    _payersState.value?.size == 1 -> {
                        expectedAmount.plus(amountBorrowed).abs().toPlainString()
                    }
                    else -> {
                        value.amount.toPlainString()
                    }
                }

                _totalExpenseAmount.value = value.amount.toPlainString()
            }
            field = value
        }

    fun setPayerAmount(participant: Participant, amount: String) {
        _payersState.value?.let { payers ->
            val newPayer = CustomSplitParticipant(
                parseNumber(amount)?.let { participant.copy(amount = it) } ?: participant,
                amountInput = amount
            )
            try {
                _payersState.value =
                    payers.toMutableList()
                        .also { it[it.indexOfFirst { it.participant == participant }] = newPayer }
            } catch (_: Throwable) {
            }
            calculateAmountLeft()
        }
    }

    private fun parseNumber(amount: String) = try {
        BigDecimal(amount).also { _errorMessage.value = null }
    } catch (e: Throwable) {
        _errorMessage.value = "Value should be number"
        if (amount.isEmpty())
            BigDecimal.ZERO
        else
            null
    }

    fun setBorrowerAmount(participant: Participant, amount: String) {
        _borrowersState.value?.let { borrowers ->
            val newBorrower =
                CustomSplitParticipant(
                    parseNumber(amount)?.let { participant.copy(amount = it.changeSign()) }
                        ?: participant,
                    amountInput = amount
                )

            try {
                _borrowersState.value =
                    borrowers.toMutableList()
                        .also {
                            it[it.indexOfFirst { it.participant == participant }] = newBorrower
                        }
            } catch (_: Throwable) {
            }
            calculateAmountLeft()
        }
    }

    fun createExpense() {
        if (_borrowersState.value?.sumOf { it.participant.amount }?.compareTo(
                _payersState.value?.sumOf { it.participant.amount }
            ) == 0 &&
            _borrowersState.value?.any { it.amountInput.isNotEmpty() } == true &&
            _payersState.value?.any { it.amountInput.isNotEmpty() } == true
        ) {
            _errorMessage.value = null
            viewModelScope.launch {
                newExpense?.let {
                    createExpenseUseCase.addExpense(
                        it.toExpense(
                            _borrowersState.value!!.map { it.participant },
                            _payersState.value!!.map { it.participant }
                        ),
                        addExpenseEffect = _addExpenseEffect
                    )
                }
            }
        } else {
            _errorMessage.value =
                "Payers should lend exact amount of money that debtors borrows."
        }
    }

    private fun calculateAmountLeft() {
        val amountPayed = _payersState.value?.sumOf { it.participant.amount } ?: BigDecimal.ZERO
        val amountBorrowed =
            _borrowersState.value?.sumOf { it.participant.amount } ?: BigDecimal.ZERO
        val expectedAmount = newExpense?.amount ?: BigDecimal.ZERO

        when {
            amountBorrowed.abs() < expectedAmount -> {
                _amountToSplit.value = expectedAmount.plus(amountBorrowed).abs().toPlainString()
            }
            amountBorrowed.abs() > expectedAmount -> {
                _errorMessage.value = "Total expense can not exceed $expectedAmount"
            }
            amountBorrowed.abs() == expectedAmount -> {
                val amount = expectedAmount.minus(amountPayed).abs().changeSign()
                if (amount.compareTo(BigDecimal.ZERO) != 0) {
                    _amountToSplit.value = amount.toPlainString()
                } else {
                    _amountToSplit.value = null
                }
            }
        }
    }
}
