package com.baharudin.mailingapp.data.login

import com.baharudin.mailingapp.data.common.module.NetworkModule
import com.baharudin.mailingapp.data.login.remote.api.LoginApi
import com.baharudin.mailingapp.data.login.repository.LoginRepositoryImpl
import com.baharudin.mailingapp.domain.login.repository.LoginRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class LoginModule {

    @Singleton
    @Provides
    fun provideLoginApi(retrofit: Retrofit) : LoginApi{
        return retrofit.create(LoginApi::class.java)
    }
    @Singleton
    @Provides
    fun loginRepository(loginApi : LoginApi) : LoginRepository{
        return LoginRepositoryImpl(loginApi)
    }
}