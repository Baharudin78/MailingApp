package com.baharudin.mailingapp.data.login.repository

import com.baharudin.mailingapp.data.common.utils.WrappedResponse
import com.baharudin.mailingapp.data.login.remote.api.LoginApi
import com.baharudin.mailingapp.data.login.remote.dto.LoginRequest
import com.baharudin.mailingapp.data.login.remote.dto.LoginResponse
import com.baharudin.mailingapp.domain.common.base.BaseResult
import com.baharudin.mailingapp.domain.login.entity.LoginEntity
import com.baharudin.mailingapp.domain.login.repository.LoginRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginApi: LoginApi
) : LoginRepository{
    override suspend fun login(loginRequest: LoginRequest): Flow<BaseResult<LoginEntity, WrappedResponse<LoginResponse>>> {
        return flow {
            val response = loginApi.login(loginRequest)
            if (response.isSuccessful){
                val body = response.body()!!
                val loginEntity = LoginEntity(body.data?.userId!!, body.data?.token!!)
                emit(BaseResult.Success(loginEntity))
            }else{
                val type = object : TypeToken<WrappedResponse<LoginResponse>>(){}.type
                val error : WrappedResponse<LoginResponse> = Gson().fromJson(response.errorBody()!!.charStream(), type)
                error.status = response.code()
                emit(BaseResult.Error(error))
            }
        }
    }
}