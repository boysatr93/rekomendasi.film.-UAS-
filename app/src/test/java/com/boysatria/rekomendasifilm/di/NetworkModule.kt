package com.boysatria.rekomendasifilm.di

import android.content.Context
import com.boysatria.rekomendasifilm.BuildConfig
import com.boysatria.rekomendasifilm.data.remote.apiservice.MoviesService
import com.boysatria.rekomendasifilm.utils.ErrorInterceptor
import com.google.gson.Gson
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

//Module Koin untuk inisialisasi awal retrofit, okhttpclient, dan service network
val networkModule = module {
    single { createOkHttpClient(get()) }
    single { createRetrofit(get()) }

    single { createMovieService(get()) }
}


fun createOkHttpClient(context: Context): OkHttpClient {
    var cache: Cache? = null
    val cacheFile = File(context.cacheDir, "responses")
    try {
        cache = Cache(cacheFile, 10 * 1024 * 1024)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    if (BuildConfig.DEBUG){
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    }

    return OkHttpClient.Builder()
        .addInterceptor(ErrorInterceptor())
        .connectTimeout(20L, TimeUnit.SECONDS)
        .readTimeout(60L, TimeUnit.SECONDS)
        .writeTimeout(60L, TimeUnit.SECONDS)
        .addInterceptor(httpLoggingInterceptor)
        .cache(cache)
        .build()
}

fun createRetrofit(okHttpClient: OkHttpClient): Retrofit {
    val baseUrl = BuildConfig.BASE_URL
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(Gson()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .client(okHttpClient)
        .build()
}

fun createMovieService(retrofit: Retrofit) : MoviesService = retrofit.create(
    MoviesService::class.java
)