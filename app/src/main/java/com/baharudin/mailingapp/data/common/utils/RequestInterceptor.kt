package com.baharudin.mailingapp.data.common.utils

import android.content.SharedPreferences
import com.baharudin.mailingapp.core.SharedPrefs
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor constructor(
    private val pref : SharedPrefs
) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = pref.getToken()
        val newRequest = chain.request().newBuilder()
            .addHeader("x-access-token", token)
            .build()
        return chain.proceed(newRequest)
    }
}