package com.leehendryp.photoalbum.core.injection

import android.content.Context
import androidx.room.Room
import com.leehendryp.photoalbum.core.data.database.PhotoAlbumDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

private const val DATABASE_NAME = "code_challenge_database"

@Module
@InstallIn(SingletonComponent::class)
internal object DataModule {
    @Provides
    fun provideAlbumDatabase(@ApplicationContext context: Context): PhotoAlbumDatabase = Room.databaseBuilder(
        context,
        PhotoAlbumDatabase::class.java,
        DATABASE_NAME,
    ).build()
}
