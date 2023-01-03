package com.baharudin.mailingapp.presentation.main.letterout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baharudin.mailingapp.domain.common.base.BaseResult
import com.baharudin.mailingapp.domain.letter.entity.LetterEntity
import com.baharudin.mailingapp.domain.letter.usecase.GetLetterOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LetterOutViewModel @Inject constructor(
    private val letterOutUseCase: GetLetterOutUseCase
) : ViewModel() {

    private val state = MutableStateFlow<LetterOutViewState>(LetterOutViewState.Init)
    val mState : StateFlow<LetterOutViewState> get() = state

    private val _letterOut = MutableStateFlow<List<LetterEntity>>(mutableListOf())
    val letterOut : StateFlow<List<LetterEntity>> get() = _letterOut

    private fun setLoading(){
        state.value = LetterOutViewState.IsLoading(true)
    }

    private fun hideLoading(){
        state.value = LetterOutViewState.IsLoading(false)
    }

    private fun showToast(message: String){
        state.value = LetterOutViewState.ShowToast(message)
    }

    init {
       fetchLetterOut()
    }

    fun fetchLetterOut(){
        viewModelScope.launch {
            letterOutUseCase.invoke()
                .onStart {
                    setLoading()
                }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.message.toString())
                }
                .collect{ result ->
                    when(result){
                        is BaseResult.Success -> {
                            hideLoading()
                            _letterOut.value = result.data
                        }
                        is BaseResult.Error -> {
                            showToast(result.rawResponse.message)
                        }
                    }
                }
        }
    }
}
sealed class LetterOutViewState{
    object Init : LetterOutViewState()
    data class IsLoading(val isLoading : Boolean) : LetterOutViewState()
    data class ShowToast(val message : String) : LetterOutViewState()
}