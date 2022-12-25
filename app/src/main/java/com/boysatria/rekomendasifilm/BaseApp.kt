package com.boysatria.rekomendasifilm

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.boysatria.rekomendasifilm.di.dataModule
import com.boysatria.rekomendasifilm.di.networkModule
import com.boysatria.rekomendasifilm.di.repositoryModule
import com.boysatria.rekomendasifilm.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

//BaseApp merupakan class inti dari aplikasi
class BaseApp: Application() {
    override fun onCreate() {
        super.onCreate()

        //memulai koin
        startKoin {
            androidContext(applicationContext) //mendapatkan context dari application
            androidLogger(Level.ERROR) //untuk menampilkan Log
            modules(listOf(
                com.boysatria.rekomendasifilm.di.dataModule,
                com.boysatria.rekomendasifilm.di.networkModule,
                com.boysatria.rekomendasifilm.di.repositoryModule,
                com.boysatria.rekomendasifilm.di.viewModelModule
            )) //mendaftarkan module ke koin
        }

        //menonaktifkan night mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}