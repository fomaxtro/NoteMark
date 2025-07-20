package com.fomaxtro.notemark.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.room.Room
import com.fomaxtro.notemark.data.conectivity.AndroidConnectivity
import com.fomaxtro.notemark.data.database.NoteMarkDatabase
import com.fomaxtro.notemark.data.database.dao.NoteDao
import com.fomaxtro.notemark.data.database.dao.SyncDao
import com.fomaxtro.notemark.data.database.dao.SyncInfoDao
import com.fomaxtro.notemark.data.datastore.SecureSessionStorage
import com.fomaxtro.notemark.data.datastore.store.EncryptedPreferenceSerializer
import com.fomaxtro.notemark.data.datastore.store.SecurePreference
import com.fomaxtro.notemark.data.remote.HttpClientFactory
import com.fomaxtro.notemark.data.remote.datasource.AuthRemoteDataSource
import com.fomaxtro.notemark.data.remote.datasource.NoteRemoteDataSource
import com.fomaxtro.notemark.data.remote.datasource.UserRemoteDataSource
import com.fomaxtro.notemark.data.repository.AuthRepositoryImpl
import com.fomaxtro.notemark.data.repository.NoteRepositoryImpl
import com.fomaxtro.notemark.data.repository.UserRepositoryImpl
import com.fomaxtro.notemark.data.sync.SyncController
import com.fomaxtro.notemark.data.validator.AndroidPatternMatching
import com.fomaxtro.notemark.domain.conectivity.Connectivity
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

    singleOf(::AuthRemoteDataSource)
    singleOf(::NoteRemoteDataSource)
    singleOf(::UserRemoteDataSource)

    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
    singleOf(::NoteRepositoryImpl).bind<NoteRepository>()
    singleOf(::UserRepositoryImpl).bind<UserRepository>()

    single<DataStore<SecurePreference>> {
        DataStoreFactory.create(
            serializer = EncryptedPreferenceSerializer,
            produceFile = {
                androidContext().dataStoreFile("secure_data.pb")
            }
        )
    }
    singleOf(::SecureSessionStorage)

    single<NoteMarkDatabase> {
        Room.databaseBuilder(
            androidContext(),
            NoteMarkDatabase::class.java,
            "notemark"
        ).build()
    }
    single<NoteDao> { get<NoteMarkDatabase>().noteDao() }
    single<SyncDao> { get<NoteMarkDatabase>().syncDao() }
    single<SyncInfoDao> { get<NoteMarkDatabase>().syncInfoDao() }

    singleOf(::AndroidConnectivity).bind<Connectivity>()

    singleOf(::SyncController)
}