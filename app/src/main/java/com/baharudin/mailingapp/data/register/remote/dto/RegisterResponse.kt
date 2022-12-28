package com.baharudin.mailingapp.data.register.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("__v") val code: Int,
    @SerializedName("_id") val id: String,
    @SerializedName("createdAt")val createdAt: String,
    @SerializedName("email")val email: String,
    @SerializedName("name")val name: String,
    @SerializedName("password")val password: String,
    @SerializedName("phone")val phone: String,
    @SerializedName("role")val role: String
)
