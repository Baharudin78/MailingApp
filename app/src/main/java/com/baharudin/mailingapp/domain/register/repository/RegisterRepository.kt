package com.baharudin.mailingapp.domain.register.repository

import com.baharudin.mailingapp.data.common.utils.WrappedResponse
import com.baharudin.mailingapp.data.register.remote.dto.RegisterRequest
import com.baharudin.mailingapp.data.register.remote.dto.RegisterResponse
import com.baharudin.mailingapp.domain.common.base.BaseResult
import com.baharudin.mailingapp.domain.register.entity.RegisterEntity
import kotlinx.coroutines.flow.Flow

interface RegisterRepository{
    suspend fun register(registerRequest: RegisterRequest) : Flow<BaseResult<RegisterEntity, WrappedResponse<RegisterResponse>>>
}