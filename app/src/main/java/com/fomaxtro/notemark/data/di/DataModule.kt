package com.fomaxtro.notemark.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.room.Room
import com.fomaxtro.notemark.data.database.NoteMarkDatabase
import com.fomaxtro.notemark.data.database.dao.NoteDao
import com.fomaxtro.notemark.data.datastore.SecureSessionStorage
import com.fomaxtro.notemark.data.datastore.impl.DataStoreSecureSessionStorage
import com.fomaxtro.notemark.data.datastore.store.EncryptedPreferenceSerializer
import com.fomaxtro.notemark.data.datastore.store.SecurePreference
import com.fomaxtro.notemark.data.remote.HttpClientFactory
import com.fomaxtro.notemark.data.remote.datasource.AuthDataSource
import com.fomaxtro.notemark.data.remote.datasource.NoteDataSource
import com.fomaxtro.notemark.data.remote.datasource.UserDataSource
import com.fomaxtro.notemark.data.remote.impl.KtorAuthDataSource
import com.fomaxtro.notemark.data.remote.impl.KtorNoteDataSource
import com.fomaxtro.notemark.data.remote.impl.KtorUserDataSource
import com.fomaxtro.notemark.data.repository.AuthRepositoryImpl
import com.fomaxtro.notemark.data.repository.NoteRepositoryImpl
import com.fomaxtro.notemark.data.repository.UserRepositoryImpl
import com.fomaxtro.notemark.data.validator.AndroidPatternMatching
import com.fomaxtro.notemark.domain.repository.AuthRepository
import com.fomaxtro.notemark.domain.repository.NoteRepository
import com.fomaxtro.notemark.domain.repository.UserRepository
import com.fomaxtro.notemark.domain.validator.PatternMatching
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single<CoroutineScope> {
        CoroutineScope(Dispatchers.Default + SupervisorJob())
    }
    singleOf(::AndroidPatternMatching).bind<PatternMatching>()

    single<HttpClient> {
        HttpClientFactory.create(
            sessionStorage = get()
        )
    }

    singleOf(::KtorAuthDataSource).bind<AuthDataSource>()
    singleOf(::KtorNoteDataSource).bind<NoteDataSource>()
    singleOf(::KtorUserDataSource).bind<UserDataSource>()

    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
    singleOf(::NoteRepositoryImpl).bind<NoteRepository>()
    singleOf(::UserRepositoryImpl).bind<UserRepository>()

    single<DataStore<SecurePreference>>(named("secure")) {
        DataStoreFactory.create(
            serializer = EncryptedPreferenceSerializer,
            produceFile = {
                androidContext().dataStoreFile("secure_data.pb")
            }
        )
    }
    singleOf(::DataStoreSecureSessionStorage).bind<SecureSessionStorage>()

    single<NoteMarkDatabase> {
        Room.databaseBuilder(
            androidContext(),
            NoteMarkDatabase::class.java,
            "notemark"
        ).build()
    }
    single<NoteDao> { get<NoteMarkDatabase>().noteDao() }
}