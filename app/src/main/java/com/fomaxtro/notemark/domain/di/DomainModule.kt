package com.fomaxtro.notemark.domain.di

import com.fomaxtro.notemark.domain.use_case.Logout
import com.fomaxtro.notemark.domain.validator.LoginDataValidator
import com.fomaxtro.notemark.domain.validator.RegistrationDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::RegistrationDataValidator)
    singleOf(::LoginDataValidator)
    singleOf(::Logout)
}