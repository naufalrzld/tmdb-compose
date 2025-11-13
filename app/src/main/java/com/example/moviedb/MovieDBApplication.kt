package com.example.moviedb

import android.app.Application
import com.example.moviedb.core.di.coreModule
import com.example.moviedb.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MovieDBApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MovieDBApplication)
            modules(coreModule, mainModule)
        }
    }
}