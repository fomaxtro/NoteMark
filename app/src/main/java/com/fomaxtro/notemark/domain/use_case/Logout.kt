package com.fomaxtro.notemark.domain.use_case

import com.fomaxtro.notemark.domain.repository.AuthRepository
import com.fomaxtro.notemark.domain.repository.NoteRepository
import com.fomaxtro.notemark.domain.repository.SyncRepository
import kotlinx.coroutines.flow.collect

class Logout(
    private val syncRepository: SyncRepository,
    private val noteRepository: NoteRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        syncRepository
            .performFullSync()
            .collect()
        noteRepository.deleteAll()
        authRepository.logout()
    }
}