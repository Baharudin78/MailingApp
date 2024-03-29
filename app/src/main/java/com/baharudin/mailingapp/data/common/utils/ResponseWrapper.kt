package com.baharudin.mailingapp.data.common.utils

import com.google.gson.annotations.SerializedName

data class WrappedResponse<T> (
    @SerializedName("message") var message : String,
    @SerializedName("statusCode") var status : Int?,
    @SerializedName("data") var data : T? = null
)

data class WrappedListResponse<T> (
    @SerializedName("message") var message : String,
    @SerializedName("status") var status : Int,
    @SerializedName("data") var data : List<T>? = null
)