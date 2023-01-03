package com.baharudin.mailingapp.data.letter.remote.api

import com.baharudin.mailingapp.data.common.utils.WrappedListResponse
import com.baharudin.mailingapp.data.common.utils.WrappedResponse
import com.baharudin.mailingapp.data.letter.remote.dto.LetterDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface LetterApi {
    @GET("api/letter/in")
    suspend fun getLetterIn() : Response<WrappedListResponse<LetterDto>>

    @GET("api/letter/out")
    suspend fun getLetterOut() : Response<WrappedListResponse<LetterDto>>

    @Multipart
    @POST("api/letter")
    suspend fun addLetter(
        @PartMap param: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part foto : MultipartBody.Part,
    ) : Response<WrappedResponse<LetterDto>>

}