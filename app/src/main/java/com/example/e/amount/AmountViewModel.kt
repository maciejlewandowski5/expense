package com.example.e.amount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e.addexpense.AddExpenseUseCase
import com.example.e.addexpense.model.AddExpenseEffect
import com.example.e.addexpense.model.NewExpenseInput
import com.example.e.domain.Expense
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class AmountViewModel @Inject constructor(private val addExpenseUseCase: AddExpenseUseCase) :
    ViewModel() {

    private val _errorMessage: MutableLiveData<String?> = MutableLiveData(null)
    val errorMessage: LiveData<String?> = _errorMessage

    private val _proposedExpense: MutableLiveData<Expense?> = MutableLiveData(null)
    val proposedExpense: LiveData<Expense?> = _proposedExpense

    private val _addExpenseEffect: MutableLiveData<AddExpenseEffect?> = MutableLiveData(null)
    val addExpenseEffect: LiveData<AddExpenseEffect?> = _addExpenseEffect

    private val _amount: MutableLiveData<String> =
        MutableLiveData(BigDecimal.ZERO.toPlainString())
    val amount: LiveData<String> = _amount

    var expenseInput: NewExpenseInput? = null

    private fun validateAmount(amount: String) = (
        !amount.contains('-')
            .and(!amount.contains(','))
            .and(!amount.contains(' '))
            .and(!amount.contains('\n'))
        )

    fun amountSet(amount: String) {
        _amount.value = amount
        if (validateAmount(amount))
            try {
                _errorMessage.value = null
                _proposedExpense.value =
                    expenseInput?.let { it.copy(amount = BigDecimal(amount)).toExpense() }
            } catch (e: Throwable) {
                _errorMessage.value = "Value should be number"
            }
        else _errorMessage.value = "Value should be number"
    }

    fun addExpense() {
        viewModelScope.launch {
            _proposedExpense.value?.let { addExpenseUseCase.addExpense(it, _addExpenseEffect) }
        }
    }
}
