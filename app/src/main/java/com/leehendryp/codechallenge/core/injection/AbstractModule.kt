package com.leehendryp.codechallenge.core.injection

import com.leehendryp.codechallenge.core.utils.NetworkChecker
import com.leehendryp.codechallenge.core.utils.NetworkCheckerImpl
import com.leehendryp.codechallenge.core.utils.NetworkStatusHelper
import com.leehendryp.codechallenge.core.utils.NetworkStatusHelperImpl
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