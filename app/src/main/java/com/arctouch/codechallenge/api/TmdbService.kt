package com.arctouch.codechallenge.api

import android.util.Log
import com.arctouch.codechallenge.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object TmdbService {

    val service: TmdbApi = Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .client(httpClient())
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()
        .create(TmdbApi::class.java)

    private fun httpClient(): OkHttpClient {
        with(OkHttpClient.Builder()) {
            if (BuildConfig.DEBUG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(logging)
            }

            try {
                val tlsSocket = TLSSocketFactory()
                sslSocketFactory(tlsSocket, tlsSocket.trustManager)
            } catch (e: Exception) {
                Log.d(TmdbService::class.java.simpleName, e.message ?: "Error initializing TLS Factory")
            }

            return this.build()
        }
    }
}