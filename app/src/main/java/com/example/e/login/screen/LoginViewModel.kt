package com.example.e.login.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e.source.SwitchSourceCardContract
import com.example.e.source.SwitchSourceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject

@HiltViewModel
@ExperimentalSerializationApi
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val switchSourceUseCase: SwitchSourceUseCase
) : ViewModel(), SwitchSourceCardContract {

    private val _userName: MutableLiveData<String> = MutableLiveData("")
    val userName: LiveData<String> = _userName

    private val _password: MutableLiveData<String> = MutableLiveData("")
    val password: LiveData<String> = _password

    private val _loginEffect: MutableLiveData<LoginEffect> = MutableLiveData(null)
    val loginEffect: LiveData<LoginEffect> = _loginEffect

    private val _isSourceRemote: MutableLiveData<Boolean> =
        MutableLiveData(switchSourceUseCase.getSource())
    val isSourceRemote: LiveData<Boolean> = _isSourceRemote

    fun setUserName(newUserName: String) {
        _userName.value = newUserName
    }

    fun setPassword(newPassword: String) {
        _password.value = newPassword
    }

    fun loginClick() {
        viewModelScope.launch {
            _loginEffect.value = LoginEffect.LoginInProgress
            if (!userName.value.isNullOrBlank() && !password.value.isNullOrBlank()) {
                _loginEffect.value = loginUseCase.login(userName.value!!, password.value!!)
            } else {
                _loginEffect.value =
                    LoginEffect.ShowErrorMessage(
                        IllegalArgumentException("Username or password can not be blank")
                    )
            }
        }
    }

    fun isSourceRemote(): Boolean = _isSourceRemote.value == true

    override fun switchSource(isSourceRemote: Boolean) {
        _isSourceRemote.value = isSourceRemote
        viewModelScope.launch {
            switchSourceUseCase.switchSource(isSourceRemote)
        }
    }

    override fun onSourceSwitchedToTrue() {}

    override fun onSourceSwitchedToFalse() {
        _loginEffect.value = LoginEffect.GoToMainScreen
    }
}
