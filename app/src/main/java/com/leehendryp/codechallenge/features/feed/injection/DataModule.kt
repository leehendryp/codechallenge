package com.leehendryp.codechallenge.features.feed.injection

import com.leehendryp.codechallenge.features.feed.data.AlbumRepositoryImpl
import com.leehendryp.codechallenge.features.feed.data.remote.RemoteDataSource
import com.leehendryp.codechallenge.features.feed.domain.AlbumRepository
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
    ): AlbumRepository = AlbumRepositoryImpl(remoteDataSource)

    @Provides
    fun provideCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
