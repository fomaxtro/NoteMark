package com.fomaxtro.notemark.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fomaxtro.notemark.data.database.dao.NoteDao
import com.fomaxtro.notemark.data.database.entity.NoteEntity

@Database(
    entities = [
        NoteEntity::class
    ],
    version = 1
)
abstract class NoteMarkDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
}