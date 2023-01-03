package com.baharudin.mailingapp.domain.letter.repository

import com.baharudin.mailingapp.data.common.utils.WrappedListResponse
import com.baharudin.mailingapp.data.common.utils.WrappedResponse
import com.baharudin.mailingapp.data.letter.remote.dto.LetterDto
import com.baharudin.mailingapp.domain.common.base.BaseResult
import com.baharudin.mailingapp.domain.letter.entity.LetterEntity
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface LetterRepository {
    suspend fun getLetterIn() : Flow<BaseResult<List<LetterEntity>, WrappedListResponse<LetterDto>>>
    suspend fun getLetterOut() : Flow<BaseResult<List<LetterEntity>, WrappedListResponse<LetterDto>>>
    suspend fun postLetter(
        param : HashMap<String, @JvmSuppressWildcards RequestBody>,
        partFile: MultipartBody.Part?,
    ) : Flow<BaseResult<LetterEntity, WrappedResponse<LetterDto>>>
}