package com.boysatria.rekomendasifilm.di

import com.boysatria.rekomendasifilm.data.local.room.repo.RoomRepo
import com.boysatria.rekomendasifilm.data.repo.MoviesRepo
import org.koin.dsl.module

//Module Koin untuk inisialisasi awal untuk Repo
val repositoryModule = module {
    factory { MoviesRepo(get()) }
    factory { RoomRepo(get()) }
}
