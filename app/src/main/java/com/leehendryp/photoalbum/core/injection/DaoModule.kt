package com.leehendryp.photoalbum.core.injection

import com.leehendryp.photoalbum.core.data.database.PhotoAlbumDatabase
import com.leehendryp.photoalbum.features.common.data.local.AlbumDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {
    @Provides
    fun provideAlbumDao(database: PhotoAlbumDatabase): AlbumDao = database.albumDao()
}
