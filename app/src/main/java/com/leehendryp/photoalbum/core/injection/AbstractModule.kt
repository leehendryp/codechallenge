package com.leehendryp.photoalbum.core.injection

import com.leehendryp.photoalbum.core.utils.NetworkChecker
import com.leehendryp.photoalbum.core.utils.NetworkCheckerImpl
import com.leehendryp.photoalbum.core.utils.NetworkStatusHelper
import com.leehendryp.photoalbum.core.utils.NetworkStatusHelperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AbstractModule {

    @Binds
    abstract fun bindNetworkChecker(impl: NetworkCheckerImpl): NetworkChecker

    @Binds
    abstract fun bindNetworkStatusHelper(impl: NetworkStatusHelperImpl): NetworkStatusHelper
}
