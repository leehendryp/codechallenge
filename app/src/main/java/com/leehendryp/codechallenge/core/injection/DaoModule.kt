package com.leehendryp.codechallenge.core.injection

import com.leehendryp.codechallenge.core.data.database.CodeChallengeDatabase
import com.leehendryp.codechallenge.features.list.data.local.AlbumDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {
    @Provides
    fun provideAlbumDao(database: CodeChallengeDatabase): AlbumDao = database.albumDao()
}
