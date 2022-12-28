package com.baharudin.mailingapp.domain.letter.usecase

import com.baharudin.mailingapp.data.common.utils.WrappedListResponse
import com.baharudin.mailingapp.data.letter.remote.dto.LetterDto
import com.baharudin.mailingapp.domain.common.base.BaseResult
import com.baharudin.mailingapp.domain.letter.entity.LetterEntity
import com.baharudin.mailingapp.domain.letter.repository.LetterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLetterInUseCase @Inject constructor(
    private val repository: LetterRepository
) {
    suspend fun invoke() : Flow<BaseResult<List<LetterEntity>, WrappedListResponse<LetterDto>>>{
        return repository.getLetterIn()
    }
}