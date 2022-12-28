package com.baharudin.mailingapp.data.register.repository

import com.baharudin.mailingapp.data.common.utils.WrappedResponse
import com.baharudin.mailingapp.data.register.remote.api.RegisterApi
import com.baharudin.mailingapp.data.register.remote.dto.RegisterRequest
import com.baharudin.mailingapp.data.register.remote.dto.RegisterResponse
import com.baharudin.mailingapp.domain.common.base.BaseResult
import com.baharudin.mailingapp.domain.register.entity.RegisterEntity
import com.baharudin.mailingapp.domain.register.repository.RegisterRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val registerApi: RegisterApi
) : RegisterRepository{
    override suspend fun register(registerRequest: RegisterRequest): Flow<BaseResult<RegisterEntity, WrappedResponse<RegisterResponse>>> {
        return flow {
            val response = registerApi.register(registerRequest)
            if (response.isSuccessful){
                val body = response.body()!!
                val registerEntity = RegisterEntity(
                    body.data?.code!!,
                    body.data?.id!!,
                    body.data?.createdAt!!,
                    body.data?.email!!,
                    body.data?.name!!,
                    body.data?.password!!,
                    body.data?.phone!!,
                    body.data?.role!!
                )
                emit(BaseResult.Success(registerEntity))
            }else{
                val type = object : TypeToken<WrappedResponse<RegisterResponse>>(){}.type
                val error : WrappedResponse<RegisterResponse> = Gson().fromJson(response.errorBody()!!.charStream(), type)
                error.status = response.code()
                emit(BaseResult.Error(error))
            }
        }
    }
}