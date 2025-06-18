package com.fomaxtro.notemark.data.di

import com.fomaxtro.notemark.data.remote.HttpClientFactory
import com.fomaxtro.notemark.data.remote.datasource.AuthDataSource
import com.fomaxtro.notemark.data.remote.impl.KtorAuthDataSource
import com.fomaxtro.notemark.data.repository.AuthRepositoryImpl
import com.fomaxtro.notemark.data.validator.AndroidPatternMatching
import com.fomaxtro.notemark.domain.repository.AuthRepository
import com.fomaxtro.notemark.domain.validator.PatternMatching
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    singleOf(::AndroidPatternMatching).bind<PatternMatching>()
    single<HttpClient> { HttpClientFactory.create() }
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
    singleOf(::KtorAuthDataSource).bind<AuthDataSource>()
}