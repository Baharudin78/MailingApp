package com.baharudin.mailingapp.domain.letter.usecase

import com.baharudin.mailingapp.data.common.utils.WrappedResponse
import com.baharudin.mailingapp.data.letter.remote.dto.LetterDto
import com.baharudin.mailingapp.domain.common.base.BaseResult
import com.baharudin.mailingapp.domain.letter.entity.LetterEntity
import com.baharudin.mailingapp.domain.letter.repository.LetterRepository
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class PostLetterUseCase @Inject constructor(
    private val letterRepository: LetterRepository
) {
    suspend fun invoke(param : MutableMap<String, @JvmSuppressWildcards RequestBody>, partFile : MultipartBody.Part?) : Flow<BaseResult<LetterEntity, WrappedResponse<LetterDto>>> {
        return letterRepository.postLetter(param, partFile)
    }
}