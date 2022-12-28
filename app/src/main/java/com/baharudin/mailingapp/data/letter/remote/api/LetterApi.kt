package com.baharudin.mailingapp.data.letter.remote.api

import com.baharudin.mailingapp.data.common.utils.WrappedListResponse
import com.baharudin.mailingapp.data.letter.remote.dto.LetterDto
import retrofit2.Response
import retrofit2.http.GET

interface LetterApi {
    @GET("api/letter/in")
    suspend fun getLetterIn() : Response<WrappedListResponse<LetterDto>>

    @GET("api/letter/out")
    suspend fun getLetterOut() : Response<WrappedListResponse<LetterDto>>

}