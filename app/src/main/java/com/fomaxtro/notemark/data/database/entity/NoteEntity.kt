package com.fomaxtro.notemark.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey val id: String,
    val title: String,
    val content: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "last_edited_at") val lastEditedAt: Long
)
