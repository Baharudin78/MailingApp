package com.baharudin.mailingapp.data.login

import com.baharudin.mailingapp.data.common.module.NetworkModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class LoginModule {

}