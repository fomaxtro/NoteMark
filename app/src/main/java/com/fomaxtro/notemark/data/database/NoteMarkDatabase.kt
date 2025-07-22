package com.fomaxtro.notemark.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import com.fomaxtro.notemark.data.database.dao.NoteDao
import com.fomaxtro.notemark.data.database.dao.SyncDao
import com.fomaxtro.notemark.data.database.dao.SyncInfoDao
import com.fomaxtro.notemark.data.database.dao.UserPreferencesDao
import com.fomaxtro.notemark.data.database.entity.NoteEntity
import com.fomaxtro.notemark.data.database.entity.SyncEntity
import com.fomaxtro.notemark.data.database.entity.SyncInfoEntity
import com.fomaxtro.notemark.data.database.entity.UserPreferencesEntity

@Database(
    entities = [
        NoteEntity::class,
        SyncEntity::class,
        SyncInfoEntity::class,
        UserPreferencesEntity::class
    ],
    version = 2,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
abstract class NoteMarkDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun syncDao(): SyncDao
    abstract fun syncInfoDao(): SyncInfoDao // Add other DAO interfaces as needed
    abstract fun userPreferencesDao(): UserPreferencesDao
}