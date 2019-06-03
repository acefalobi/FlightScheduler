package com.aceinteract.flightscheduler

import android.app.Application
import android.content.Context
import com.aceinteract.flightscheduler.data.remote.repository.AirlineRemoteRepository
import com.aceinteract.flightscheduler.data.remote.repository.AirportRemoteRepository
import com.aceinteract.flightscheduler.data.remote.repository.AuthRemoteRepository
import com.aceinteract.flightscheduler.data.remote.repository.FlightRemoteRepository
import com.aceinteract.flightscheduler.util.StorageUtil
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

/**
 * Main application class controlling the entire app module
 */
class App : Application() {

    private val appModule = module {
        single { StorageUtil(get()) }
    }

    private val repoModule = module {
        factory { AuthRemoteRepository() }
        factory {
            AirportRemoteRepository(
                get<Context>().assets.open("airports.json").bufferedReader().use { it.readText() },
                get<StorageUtil>().accessToken
            )
        }
        factory {
            FlightRemoteRepository(
                get<Context>().assets.open("airports.json").bufferedReader().use { it.readText() },
                get<StorageUtil>().accessToken
            )
        }
        factory { AirlineRemoteRepository(get<StorageUtil>().accessToken) }
    }

    /**
     * Setup Koin di.
     */
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(appModule, repoModule)
        }
    }

}