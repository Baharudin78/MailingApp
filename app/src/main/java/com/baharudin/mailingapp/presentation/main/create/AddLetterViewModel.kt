package com.baharudin.mailingapp.presentation.main.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baharudin.mailingapp.domain.common.base.BaseResult
import com.baharudin.mailingapp.domain.letter.usecase.PostLetterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AddLetterViewModel @Inject constructor(
    private val postLetterUseCase: PostLetterUseCase
): ViewModel() {

    private val state = MutableStateFlow<AddLetterViewState>(AddLetterViewState.Init)
    val mState: StateFlow<AddLetterViewState> get() = state

    private fun setLoading(){
        state.value = AddLetterViewState.IsLoading(true)
    }

    private fun hideLoading(){
        state.value = AddLetterViewState.IsLoading(false)
    }

    private fun showToast(message: String){
        state.value = AddLetterViewState.ShowToast(message)
    }

    private fun successCreate(){
        state.value = AddLetterViewState.SuccessCreate
    }

    fun uploadLetter(
        param : HashMap<String, @JvmSuppressWildcards RequestBody>,
        partFile : MultipartBody.Part?,
    ){
        viewModelScope.launch {
            postLetterUseCase.invoke(param, partFile)
                .onStart {
                    setLoading()
                }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.message.toString())
                }
                .collect{ result ->
                    when(result) {
                        is BaseResult.Success -> {
                            hideLoading()
                            successCreate()
                        }
                        is BaseResult.Error -> {
                            showToast(result.rawResponse.message)
                        }
                    }
                }
        }
    }
}

sealed class AddLetterViewState{
    object Init : AddLetterViewState()
    object SuccessCreate : AddLetterViewState()
    data class IsLoading(val isLoading : Boolean) : AddLetterViewState()
    data class ShowToast(val message : String) : AddLetterViewState()
}

