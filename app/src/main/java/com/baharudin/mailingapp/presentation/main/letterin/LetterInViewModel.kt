package com.baharudin.mailingapp.presentation.main.letterin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baharudin.mailingapp.domain.common.base.BaseResult
import com.baharudin.mailingapp.domain.letter.entity.LetterEntity
import com.baharudin.mailingapp.domain.letter.usecase.GetLetterInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LetterInViewModel @Inject constructor(
    private val letterInUseCase: GetLetterInUseCase
) : ViewModel(){

    private val state = MutableStateFlow<LetterInViewState>(LetterInViewState.Init)
    val mState: StateFlow<LetterInViewState> get() = state

    private val products = MutableStateFlow<List<LetterEntity>>(mutableListOf())
    val mProducts: StateFlow<List<LetterEntity>> get() = products

    private fun setLoading(){
        state.value = LetterInViewState.IsLoading(true)
    }

    private fun hideLoading(){
        state.value = LetterInViewState.IsLoading(false)
    }

    private fun showToast(message: String){
        state.value = LetterInViewState.ShowToast(message)
    }

    init {
        fetchAllMyProducts()
    }

    fun fetchAllMyProducts(){
        viewModelScope.launch {
            letterInUseCase.invoke()
                .onStart {
                    setLoading()
                }
                .catch { exception ->
                    hideLoading()
                    showToast(exception.message.toString())
                }
                .collect { result ->
                    hideLoading()
                    when(result){
                        is BaseResult.Success -> {
                            products.value = result.data
                        }
                        is BaseResult.Error -> {
                            showToast(result.rawResponse.message)
                        }
                    }
                }
        }
    }
}
sealed class LetterInViewState {
    object Init : LetterInViewState()
    data class IsLoading(val isLoading: Boolean) : LetterInViewState()
    data class ShowToast(val message : String) : LetterInViewState()
}