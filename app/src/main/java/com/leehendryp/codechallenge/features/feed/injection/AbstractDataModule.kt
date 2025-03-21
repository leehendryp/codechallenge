package com.leehendryp.codechallenge.features.feed.injection

import com.leehendryp.codechallenge.features.feed.data.remote.RemoteDataSource
import com.leehendryp.codechallenge.features.feed.data.remote.RemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AbstractDataModule {
    @Binds
    abstract fun bindRemoteDataSource(impl: RemoteDataSourceImpl): RemoteDataSource
}
