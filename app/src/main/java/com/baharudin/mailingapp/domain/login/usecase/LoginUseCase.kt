package com.baharudin.mailingapp.domain.login.usecase

import com.baharudin.mailingapp.data.common.utils.WrappedResponse
import com.baharudin.mailingapp.data.login.remote.dto.LoginRequest
import com.baharudin.mailingapp.data.login.remote.dto.LoginResponse
import com.baharudin.mailingapp.domain.common.base.BaseResult
import com.baharudin.mailingapp.domain.login.entity.LoginEntity
import com.baharudin.mailingapp.domain.login.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    suspend fun execute(loginRequest: LoginRequest) : Flow<BaseResult<LoginEntity, WrappedResponse<LoginResponse>>>{
        return repository.login(loginRequest)
    }
}