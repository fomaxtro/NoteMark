package com.fomaxtro.notemark.presentation.di

import com.fomaxtro.notemark.presentation.screen.landing.LandingViewModel
import com.fomaxtro.notemark.presentation.screen.login.LoginViewModel
import com.fomaxtro.notemark.presentation.screen.registration.RegistrationViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::LandingViewModel)
    viewModelOf(::RegistrationViewModel)
    viewModelOf(::LoginViewModel)
}