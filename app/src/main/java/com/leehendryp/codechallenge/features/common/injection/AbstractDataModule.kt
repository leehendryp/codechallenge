package com.leehendryp.codechallenge.features.common.injection

import com.leehendryp.codechallenge.features.common.data.local.LocalDataSource
import com.leehendryp.codechallenge.features.common.data.local.LocalDataSourceImpl
import com.leehendryp.codechallenge.features.common.data.remote.RemoteDataSource
import com.leehendryp.codechallenge.features.common.data.remote.RemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AbstractDataModule {
    @Binds
    abstract fun bindRemoteDataSource(impl: RemoteDataSourceImpl): RemoteDataSource

    @Binds
    abstract fun bindLocalDataSource(impl: LocalDataSourceImpl): LocalDataSource
}
