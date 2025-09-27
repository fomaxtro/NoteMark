package com.fomaxtro.notemark.domain.use_case

import com.fomaxtro.notemark.domain.repository.AuthRepository
import com.fomaxtro.notemark.domain.repository.NoteRepository
import com.fomaxtro.notemark.domain.repository.SyncRepository
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LogoutTest {
    private lateinit var syncRepository: SyncRepository
    private lateinit var noteRepository: NoteRepository
    private lateinit var authRepository: AuthRepository
    private lateinit var logout: Logout

    @BeforeEach
    fun setUp() {
        syncRepository = mockk()
        noteRepository = mockk(relaxed = true)
        authRepository = mockk(relaxed = true)

        logout = Logout(
            syncRepository = syncRepository,
            noteRepository = noteRepository,
            authRepository = authRepository
        )
    }

    @Suppress("UnusedFlow")
    @Test
    fun `Test logout`() = runTest {
        every { syncRepository.performFullSync() } returns emptyFlow()

        logout()

        coVerifyOrder {
            syncRepository.performFullSync()
            noteRepository.deleteAll()
            authRepository.logout()
        }
    }
}