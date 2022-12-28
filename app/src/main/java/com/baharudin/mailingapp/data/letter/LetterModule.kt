package com.baharudin.mailingapp.data.letter

import com.baharudin.mailingapp.data.common.module.NetworkModule
import com.baharudin.mailingapp.data.letter.remote.api.LetterApi
import com.baharudin.mailingapp.data.letter.repository.LetterRepositoryImpl
import com.baharudin.mailingapp.domain.letter.repository.LetterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class LetterModule {
    @Provides
    @Singleton
    fun provideLetterApi(retrofit : Retrofit) : LetterApi{
        return retrofit.create(LetterApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLetterRepository(letterApi : LetterApi) : LetterRepository{
        return LetterRepositoryImpl(letterApi)
    }
}