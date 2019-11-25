package com.dali.schedulingengineers.model.api.client

import android.content.Context
import com.dali.schedulingengineers.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

abstract class BaseClient(val context: Context) {
    companion object {
        private const val TIMEOUT: Long = 60 //in seconds
    }

    protected open val httpClient: OkHttpClient.Builder = OkHttpClient.Builder().apply {
        connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        readTimeout(TIMEOUT, TimeUnit.SECONDS)

        when (BuildConfig.DEBUG) {
            true -> {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(logging)
            }
        }
    }
}