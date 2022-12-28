package com.baharudin.mailingapp.data.register

import com.baharudin.mailingapp.data.common.module.NetworkModule
import com.baharudin.mailingapp.data.register.remote.api.RegisterApi
import com.baharudin.mailingapp.data.register.repository.RegisterRepositoryImpl
import com.baharudin.mailingapp.domain.register.repository.RegisterRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class RegisterModule {

    @Singleton
    @Provides
    fun provideRegisterApi(retrofit : Retrofit) : RegisterApi{
        return retrofit.create(RegisterApi::class.java)
    }
    @Singleton
    @Provides
    fun registerRepository(registerApi: RegisterApi) : RegisterRepository{
        return RegisterRepositoryImpl(registerApi)
    }
}