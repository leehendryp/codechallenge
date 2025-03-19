package com.leehendryp.codechallenge.core.injection

import com.leehendryp.codechallenge.core.data.ktorClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
    @Provides
    fun provideHttpClient(): HttpClient = ktorClient
}
