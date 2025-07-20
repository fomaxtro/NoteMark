package com.fomaxtro.notemark.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class SyncOperation {
    INSERT,
    UPDATE,
    DELETE
}

@Entity(tableName = "syncs")
data class SyncEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "user_id") val userId: String,
    val operation: SyncOperation,
    @Embedded(prefix = "local_") val note: NoteEntity,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis()
)
