package com.fomaxtro.notemark.data.repository

import com.fomaxtro.notemark.data.database.dao.NoteDao
import com.fomaxtro.notemark.data.mapper.toNoteDto
import com.fomaxtro.notemark.data.mapper.toNoteEntity
import com.fomaxtro.notemark.data.remote.datasource.NoteDataSource
import com.fomaxtro.notemark.domain.model.Note
import com.fomaxtro.notemark.domain.repository.NoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
    private val noteDataSource: NoteDataSource,
    private val applicationScope: CoroutineScope
) : NoteRepository {
    override suspend fun createNote(note: Note) {
        noteDao.insert(note.toNoteEntity())

        applicationScope.launch {
            noteDataSource.createNote(note.toNoteDto())
        }
    }
}
