package com.leehendryp.codechallenge.core.injection

import android.content.Context
import android.net.ConnectivityManager
import com.leehendryp.codechallenge.core.CodeChallengeApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {
    @Provides
    fun provideApplicationContext(@ApplicationContext context: Context): Context = context as CodeChallengeApp

    @Provides
    fun provideConnectivityManager(context: Context): ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
}
