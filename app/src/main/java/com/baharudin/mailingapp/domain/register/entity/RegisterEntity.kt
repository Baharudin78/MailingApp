package com.baharudin.mailingapp.domain.register.entity

data class RegisterEntity(
    val code: Int,
    val id: String,
    val createdAt: String,
    val email: String,
    val name: String,
    val password: String,
    val phone: String,
    val role: String
)
