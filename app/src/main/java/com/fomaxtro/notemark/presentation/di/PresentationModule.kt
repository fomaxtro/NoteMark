package com.fomaxtro.notemark.presentation.di

import com.fomaxtro.notemark.R
import com.fomaxtro.notemark.presentation.screen.edit_note.EditNoteViewModel
import com.fomaxtro.notemark.presentation.screen.landing.LandingViewModel
import com.fomaxtro.notemark.presentation.screen.login.LoginViewModel
import com.fomaxtro.notemark.presentation.screen.note_list.NoteListViewModel
import com.fomaxtro.notemark.presentation.screen.registration.RegistrationViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::LandingViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::LoginViewModel)
    viewModel<NoteListViewModel> {
        val defaultTitle = androidContext().getString(R.string.note_title)

        NoteListViewModel(
            defaultTitle = defaultTitle,
            noteRepository = get()
        )
    }
    viewModel<EditNoteViewModel> { (id: String) ->
        val defaultTitle = androidContext().getString(R.string.note_title)

        EditNoteViewModel(
            id = id,
            defaultTitle = defaultTitle,
            noteRepository = get()
        )
    }
}