package com.fomaxtro.notemark.data.sync

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import com.fomaxtro.notemark.data.database.NoteMarkDatabase
import com.fomaxtro.notemark.data.database.entity.SyncEntity
import com.fomaxtro.notemark.data.database.entity.SyncOperation
import com.fomaxtro.notemark.data.datastore.SecureSessionStorage
import com.fomaxtro.notemark.data.mapper.toNoteEntity
import com.fomaxtro.notemark.data.remote.datasource.NoteRemoteDataSource
import com.fomaxtro.notemark.fake.FakeNoteDao
import com.fomaxtro.notemark.fake.SyncDaoFake
import com.fomaxtro.notemark.util.NoteFactory
import com.fomaxtro.notemark.util.TestDispatcherExtension
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@ExtendWith(TestDispatcherExtension::class)
class SyncControllerTest {
    private lateinit var syncDao: SyncDaoFake
    private lateinit var sessionStorage: SecureSessionStorage
    private lateinit var noteRemoteDataSource: NoteRemoteDataSource
    private lateinit var noteDao: FakeNoteDao
    private lateinit var database: NoteMarkDatabase
    private lateinit var syncController: SyncController

    private val userId = "1"

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp(extension: TestDispatcherExtension) {
        syncDao = SyncDaoFake(extension.testDispatcher)
        sessionStorage = mockk()
        noteRemoteDataSource = mockk()
        noteDao = FakeNoteDao(extension.testDispatcher)
        database = mockk()

        syncController = SyncController(
            syncDao = syncDao,
            sessionStorage = sessionStorage,
            noteDataSource = noteRemoteDataSource,
            noteDao = noteDao,
            database = database
        )

        coEvery { sessionStorage.getUserId() } returns userId
    }

    @Test
    fun `Test schedule insert sync operation`() = runTest {
        val note = NoteFactory.create()

        syncController.scheduleSyncOperation(note.toNoteEntity(), SyncOperation.INSERT)

        assertThat(syncDao.syncEntities).hasSize(1)

        val currentSync = syncDao.syncEntities.first()

        assertThat(currentSync.note.id).isEqualTo(note.id.toString())
        assertThat(currentSync.operation).isEqualTo(SyncOperation.INSERT)
    }

    @ParameterizedTest
    @MethodSource("updateSyncOperationTestCases")
    fun `Test schedule update sync operation on existing sync operation`(
        existingOperation: SyncOperation,
        operation: SyncOperation,
        expectedOperation: SyncOperation
    ) = runTest {
        val note = NoteFactory.create()
        val insertedSync = SyncEntity(
            id = 1,
            userId = userId,
            operation = existingOperation,
            note = note.toNoteEntity()
        )
        val updatedNote = NoteFactory.create(
            id = note.id,
            content = "updated"
        )

        syncDao.syncEntities.add(insertedSync)
        syncController.scheduleSyncOperation(updatedNote.toNoteEntity(), operation)

        assertThat(syncDao.syncEntities).hasSize(1)

        val currentSync = syncDao.syncEntities.first()

        assertThat(currentSync.note.content).isEqualTo(updatedNote.content)
        assertThat(currentSync.operation).isEqualTo(expectedOperation)
    }

    @Test
    fun `Test schedule delete sync operation on existing insert sync operation`() = runTest {
        val note = NoteFactory.create()
        val insertedSync = SyncEntity(
            id = 1,
            userId = userId,
            operation = SyncOperation.INSERT,
            note = note.toNoteEntity()
        )

        syncDao.syncEntities.add(insertedSync)
        syncController.scheduleSyncOperation(note.toNoteEntity(), SyncOperation.DELETE)

        assertThat(syncDao.syncEntities).isEmpty()
    }

    @Test
    fun `Test schedule delete sync operation on existing update sync operation`() = runTest {
        val note = NoteFactory.create()
        val insertedSync = SyncEntity(
            id = 1,
            userId = userId,
            operation = SyncOperation.UPDATE,
            note = note.toNoteEntity()
        )

        syncDao.syncEntities.add(insertedSync)
        syncController.scheduleSyncOperation(note.toNoteEntity(), SyncOperation.DELETE)

        assertThat(syncDao.syncEntities).hasSize(1)

        val currentSync = syncDao.syncEntities.first()

        assertThat(currentSync.note.id).isEqualTo(note.id.toString())
        assertThat(currentSync.operation).isEqualTo(SyncOperation.DELETE)
    }

    @Test
    fun `Test delete sync operation`() = runTest {
        val note = NoteFactory.create()

        syncController.scheduleSyncOperation(note.toNoteEntity(), SyncOperation.DELETE)

        assertThat(syncDao.syncEntities).hasSize(1)

        val currentSync = syncDao.syncEntities.first()

        assertThat(currentSync.note.id).isEqualTo(note.id.toString())
        assertThat(currentSync.operation).isEqualTo(SyncOperation.DELETE)
    }

    companion object {
        @JvmStatic
        fun updateSyncOperationTestCases() = listOf(
            Arguments.of(SyncOperation.INSERT, SyncOperation.UPDATE, SyncOperation.INSERT),
            Arguments.of(SyncOperation.UPDATE, SyncOperation.UPDATE, SyncOperation.UPDATE)
        )
    }
}