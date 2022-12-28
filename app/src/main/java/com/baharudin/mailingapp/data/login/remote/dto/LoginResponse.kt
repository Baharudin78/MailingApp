package com.baharudin.mailingapp.data.login.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("user_id") var userId : String? = null,
    @SerializedName("token") var token: String? = null
)
