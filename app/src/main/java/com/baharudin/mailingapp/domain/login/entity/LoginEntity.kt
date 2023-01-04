package com.baharudin.mailingapp.domain.login.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class LoginEntity(
    val userId : String,
    val token : String
) : Parcelable
