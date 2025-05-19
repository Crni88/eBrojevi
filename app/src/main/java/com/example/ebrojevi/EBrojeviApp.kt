package com.example.ebrojevi

import android.app.Application
import com.example.ebrojevi.additives.networkModule
import com.example.ebrojevi.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class EBrojeviApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@EBrojeviApp)
            modules(appModule, networkModule)
        }
    }
}