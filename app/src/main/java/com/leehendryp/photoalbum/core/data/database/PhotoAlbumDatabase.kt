package com.leehendryp.photoalbum.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.leehendryp.photoalbum.features.common.data.local.AlbumDao
import com.leehendryp.photoalbum.features.common.data.local.model.AlbumEntity

@Database(entities = [AlbumEntity::class], version = 1)
abstract class PhotoAlbumDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
}
