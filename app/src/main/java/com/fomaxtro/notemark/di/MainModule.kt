package com.fomaxtro.notemark.di

import com.fomaxtro.notemark.MainViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mainModule = module {
    viewModelOf(::MainViewModel)
}