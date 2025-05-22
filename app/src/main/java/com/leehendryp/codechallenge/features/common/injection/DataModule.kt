package com.leehendryp.codechallenge.features.common.injection

import com.leehendryp.codechallenge.features.common.data.AlbumRepositoryImpl
import com.leehendryp.codechallenge.features.common.data.local.LocalDataSource
import com.leehendryp.codechallenge.features.common.data.remote.RemoteDataSource
import com.leehendryp.codechallenge.features.common.domain.AlbumRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
internal object DataModule {

    @Provides
    fun provideAlbumRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
    ): AlbumRepository = AlbumRepositoryImpl(remoteDataSource, localDataSource)

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
