package com.baharudin.mailingapp.data.letter.repository

import com.baharudin.mailingapp.data.common.utils.WrappedListResponse
import com.baharudin.mailingapp.data.common.utils.WrappedResponse
import com.baharudin.mailingapp.data.letter.remote.api.LetterApi
import com.baharudin.mailingapp.data.letter.remote.dto.LetterDto
import com.baharudin.mailingapp.domain.common.base.BaseResult
import com.baharudin.mailingapp.domain.letter.entity.LetterEntity
import com.baharudin.mailingapp.domain.letter.repository.LetterRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class LetterRepositoryImpl @Inject constructor(
    private val letterApi: LetterApi
) : LetterRepository{
    override suspend fun getLetterIn(): Flow<BaseResult<List<LetterEntity>, WrappedListResponse<LetterDto>>> {
        return flow {
            val response = letterApi.getLetterIn()
            if (response.isSuccessful){
                val body = response.body()!!
                val letter = mutableListOf<LetterEntity>()
                body.data?.forEach { letterResponse ->
                    letter.add(
                        LetterEntity(
                            letterResponse.code,
                            letterResponse.id,
                            letterResponse.createdAt,
                            letterResponse.imagesLetterUrl,
                            letterResponse.letterDate,
                            letterResponse.letterDestination,
                            letterResponse.letterDiscription,
                            letterResponse.letterKinds,
                            letterResponse.letterNumber,
                            letterResponse.senderIdentity
                        )
                    )
                }
                emit(BaseResult.Success(letter))
            }else{
                val type = object : TypeToken<WrappedListResponse<LetterDto>>(){}.type
                val err = Gson().fromJson<WrappedListResponse<LetterDto>>(response.errorBody()!!.charStream(), type)!!
                err.status = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }

    override suspend fun getLetterOut(): Flow<BaseResult<List<LetterEntity>, WrappedListResponse<LetterDto>>> {
        return flow {
            val response = letterApi.getLetterOut()
            if (response.isSuccessful){
                val body = response.body()!!
                val letter = mutableListOf<LetterEntity>()
                body.data?.forEach { letterResponse ->
                    letter.add(
                        LetterEntity(
                            letterResponse.code,
                            letterResponse.id,
                            letterResponse.createdAt,
                            letterResponse.imagesLetterUrl,
                            letterResponse.letterDate,
                            letterResponse.letterDestination,
                            letterResponse.letterDiscription,
                            letterResponse.letterKinds,
                            letterResponse.letterNumber,
                            letterResponse.senderIdentity
                        )
                    )
                }
                emit(BaseResult.Success(letter))
            }else{
                val type = object : TypeToken<WrappedListResponse<LetterDto>>(){}.type
                val err = Gson().fromJson<WrappedListResponse<LetterDto>>(response.errorBody()!!.charStream(), type)!!
                err.status = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }

    override suspend fun postLetter(
        param: HashMap<String, RequestBody>,
        partFile: MultipartBody.Part?
    ): Flow<BaseResult<LetterEntity, WrappedResponse<LetterDto>>> {
        return flow {
            val response = letterApi.addLetter(param, partFile!!)
            if (response.isSuccessful){
                val body = response.body()!!
                val letter = LetterEntity(
                    body.data?.code!!,
                    body.data?.id!!,
                    body.data?.createdAt!!,
                    body.data?.imagesLetterUrl!!,
                    body.data?.letterDate!!,
                    body.data?.letterDestination!!,
                    body.data?.letterDiscription!!,
                    body.data?.letterKinds!!,
                    body.data?.letterNumber!!,
                    body.data?.senderIdentity!!
                )
                emit(BaseResult.Success(letter))
            }else{
                val type = object : TypeToken<WrappedResponse<LetterDto>>(){}.type
                val err = Gson().fromJson<WrappedResponse<LetterDto>>(response.errorBody()!!.charStream(), type)!!
                err.status = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }
}