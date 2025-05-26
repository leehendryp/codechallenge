package com.leehendryp.photoalbum.core.injection

import com.leehendryp.photoalbum.core.data.KtorClientProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android

@Module
@InstallIn(SingletonComponent::class)
internal object HttpClientModule {
    @Provides
    fun provideHttpClientEngine(): HttpClientEngine = Android.create()

    @Provides
    fun provideHttpClient(engine: HttpClientEngine): HttpClient = KtorClientProvider(engine).client
}
