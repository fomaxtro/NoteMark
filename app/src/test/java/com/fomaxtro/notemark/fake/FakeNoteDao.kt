package com.fomaxtro.notemark.fake

import com.fomaxtro.notemark.data.database.dao.NoteDao
import com.fomaxtro.notemark.data.database.entity.NoteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.withContext

class FakeNoteDao(
    private val dispatcher: TestDispatcher
) : NoteDao {
    val notes = MutableStateFlow<List<NoteEntity>>(emptyList())

    override suspend fun upsert(note: NoteEntity) {
        withContext(dispatcher) {
            val index = notes.value.indexOfFirst { it.id == note.id }

            if (index == -1) {
                notes.value += note
            } else {
                notes.value = notes.value.toMutableList().apply {
                    set(index, note)
                }
            }
        }
    }

    override suspend fun upsert(notes: List<NoteEntity>) {
        notes.forEach { upsert(it) }
    }

    override fun findById(id: String): Flow<NoteEntity> {
        return notes
            .map { notes ->
                notes.find { it.id == id }
            }
            .filterNotNull()
    }

    override suspend fun delete(note: NoteEntity) {
        withContext(dispatcher) {
            notes.value = notes.value.toMutableList().apply {
                remove(note)
            }
        }
    }

    override suspend fun delete(notes: List<NoteEntity>) {
        notes.forEach { delete(it) }
    }

    override fun getRecentNotes(): Flow<List<NoteEntity>> {
        return notes
            .map { notes ->
                notes.sortedByDescending { it.lastEditedAt }
            }
    }

    override suspend fun deleteAll() {
        withContext(dispatcher) {
            notes.value = emptyList()
        }
    }
}