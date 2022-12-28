package com.baharudin.mailingapp.data.login.remote.api

import com.baharudin.mailingapp.data.common.utils.WrappedResponse
import com.baharudin.mailingapp.data.login.remote.dto.LoginRequest
import com.baharudin.mailingapp.data.login.remote.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest) : Response<WrappedResponse<LoginResponse>>
}