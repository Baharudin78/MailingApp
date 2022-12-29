package com.baharudin.mailingapp.presentation.register

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.baharudin.mailingapp.R
import com.baharudin.mailingapp.data.common.utils.WrappedResponse
import com.baharudin.mailingapp.data.register.remote.dto.RegisterRequest
import com.baharudin.mailingapp.data.register.remote.dto.RegisterResponse
import com.baharudin.mailingapp.databinding.ActivityRegisterBinding
import com.baharudin.mailingapp.domain.register.entity.RegisterEntity
import com.baharudin.mailingapp.presentation.common.extention.isEmail
import com.baharudin.mailingapp.presentation.common.extention.showGenericAlertDialog
import com.baharudin.mailingapp.presentation.common.extention.showToast
import com.baharudin.mailingapp.presentation.login.LoginActivity
import com.baharudin.mailingapp.presentation.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding
    private val viewModel : RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        back()
        register()
        observe()
    }

    private fun register(){
        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val phone = binding.phoneEditText.text.toString().trim()
            if(validate(name, email, password, phone)){
                viewModel.register(RegisterRequest(name, email, phone,password))
            }
        }
    }

    private fun validate(name: String, email: String, password: String, phone : String) :  Boolean{
        resetAllInputError()

        if(name.isEmpty()){
            setNameError(getString(R.string.error_name_not_valid))
            return false
        }

        if(!email.isEmail()){
            setEmailError(getString(R.string.error_email_not_valid))
            return false
        }

        if(password.length < 8){
            setPasswordError(getString(R.string.error_password_not_valid))
            return false
        }

        if (phone.length >= 15) {
            setPhoneError("Nomor Terlalu banyak")
            return false
        }

        return true
    }

    private fun resetAllInputError(){
        setNameError(null)
        setEmailError(null)
        setPasswordError(null)
        setPhoneError(null)
    }

    private fun setNameError(e: String?){
        binding.nameInput.error = e
    }

    private fun setEmailError(e: String?){
        binding.emailInput.error = e
    }

    private fun setPasswordError(e: String?){
        binding.passwordInput.error = e
    }

    private fun setPhoneError(e : String?) {
        binding.phoneInput.error = e
    }

    private fun back(){
        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun handleStateChange(state: RegisterViewState){
        when(state){
            is RegisterViewState.ShowToast -> showToast(state.message)
            is RegisterViewState.IsLoading -> handleLoading(state.isLoading)
            is RegisterViewState.SuccessRegister -> handleSuccessRegister(state.registerEntity)
            is RegisterViewState.ErrorRegister -> handleErrorRegister(state.rawResponse)
            is RegisterViewState.Init -> Unit
        }
    }

    private fun observe(){
        viewModel.state
            .flowWithLifecycle(lifecycle,  Lifecycle.State.STARTED)
            .onEach { state -> handleStateChange(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleSuccessRegister(registerEntity: RegisterEntity){
        showToast("Welcome ${registerEntity.name}")
        goToLoginActivity()
    }

    private fun handleErrorRegister(httpResponse: WrappedResponse<RegisterResponse>){
        showGenericAlertDialog(httpResponse.message)
    }

    private fun goToLoginActivity(){
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun handleLoading(isLoading: Boolean){
        binding.registerButton.isEnabled = !isLoading
        binding.backButton.isEnabled = !isLoading
        binding.loadingProgressBar.isIndeterminate = isLoading
        if(!isLoading){
            binding.loadingProgressBar.progress = 0
        }
    }
}