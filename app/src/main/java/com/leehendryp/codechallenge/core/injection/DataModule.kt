package com.leehendryp.codechallenge.core.injection

import android.content.Context
import androidx.room.Room
import com.leehendryp.codechallenge.core.data.database.CodeChallengeDatabase
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
    fun provideAlbumDatabase(@ApplicationContext context: Context): CodeChallengeDatabase = Room.databaseBuilder(
        context,
        CodeChallengeDatabase::class.java,
        DATABASE_NAME,
    ).build()
}
