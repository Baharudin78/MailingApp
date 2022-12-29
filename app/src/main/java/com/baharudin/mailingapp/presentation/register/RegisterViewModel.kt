package com.baharudin.mailingapp.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baharudin.mailingapp.data.common.utils.WrappedResponse
import com.baharudin.mailingapp.data.register.remote.dto.RegisterRequest
import com.baharudin.mailingapp.data.register.remote.dto.RegisterResponse
import com.baharudin.mailingapp.domain.common.base.BaseResult
import com.baharudin.mailingapp.domain.register.entity.RegisterEntity
import com.baharudin.mailingapp.domain.register.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel(){

    private val _state = MutableStateFlow<RegisterViewState>(RegisterViewState.Init)
    val state : StateFlow<RegisterViewState> get() = _state

    private fun setLoading() {
        _state.value = RegisterViewState.IsLoading(true)
    }

    private fun hideLoading() {
        _state.value = RegisterViewState.IsLoading(false)
    }

    private fun showToast(message: String) {
        _state.value = RegisterViewState.ShowToast(message)
    }

    private fun successRegister(registerEntity: RegisterEntity) {
        _state.value = RegisterViewState.SuccessRegister(registerEntity)
    }

    private fun failedRegister(rawResponse : WrappedResponse<RegisterResponse>){
        _state.value = RegisterViewState.ErrorRegister(rawResponse)
    }

    fun register(registerRequest: RegisterRequest){
        viewModelScope.launch {
            registerUseCase.invoke(registerRequest)
                .onStart {
                    setLoading()
                }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.localizedMessage.orEmpty())
                }
                .collect{ result ->
                    when(result){
                        is BaseResult.Success -> {
                            hideLoading()
                            successRegister(result.data)
                        }
                        is BaseResult.Error -> {
                            hideLoading()
                            failedRegister(result.rawResponse)
                        }
                    }
                }
        }
    }


}

sealed class RegisterViewState{
    object Init : RegisterViewState()
    data class IsLoading(val isLoading : Boolean) : RegisterViewState()
    data class ShowToast(val message : String) : RegisterViewState()
    data class SuccessRegister(val registerEntity: RegisterEntity) : RegisterViewState()
    data class ErrorRegister(val rawResponse : WrappedResponse<RegisterResponse>) : RegisterViewState()
}