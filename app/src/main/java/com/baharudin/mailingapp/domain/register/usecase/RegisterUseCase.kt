package com.baharudin.mailingapp.domain.register.usecase

import com.baharudin.mailingapp.data.common.utils.WrappedResponse
import com.baharudin.mailingapp.data.register.remote.dto.RegisterRequest
import com.baharudin.mailingapp.data.register.remote.dto.RegisterResponse
import com.baharudin.mailingapp.domain.common.base.BaseResult
import com.baharudin.mailingapp.domain.register.entity.RegisterEntity
import com.baharudin.mailingapp.domain.register.repository.RegisterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository : RegisterRepository
) {
    suspend fun invoke(registerRequest: RegisterRequest) : Flow<BaseResult<RegisterEntity, WrappedResponse<RegisterResponse>>>{
        return repository.register(registerRequest)
    }
}