package com.leehendryp.codechallenge.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leehendryp.codechallenge.features.common.data.local.AlbumDao
import com.leehendryp.codechallenge.features.common.data.local.model.AlbumEntity

@Database(entities = [AlbumEntity::class], version = 1)
abstract class CodeChallengeDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
}
