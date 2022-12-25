package com.boysatria.rekomendasifilm.di

import android.content.Context
import com.boysatria.rekomendasifilm.data.local.room.MainRoomDatabase

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

//Module Koin untuk inisialisasi awal Room

val dataModule = module {
    single { createRoomDatabase(androidContext()) }
}

fun createRoomDatabase(context: Context) : MainRoomDatabase = MainRoomDatabase.getInstance(context)!!
