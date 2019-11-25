package com.dali.schedulingengineers.model.api.client

import android.content.Context
import com.dali.schedulingengineers.utils.SingletonContextHolder
import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestClient(context: Context) : BaseClient(context) {

    companion object : SingletonContextHolder<RestClient, Context>(::RestClient) {
        const val HEADER_CONTENT_TYPE_JSON = "Content-Type: application/json"
        const val HEADER_CACHE_CONTROL_NO_CACHE = "Cache-Control: no-cache"
    }

    fun <T> createService(
        serviceClass: Class<T>,
        gson: Gson,
        serverURL: String
    ): T {
        val builder = Retrofit.Builder()
            .baseUrl(serverURL)
            .addConverterFactory(GsonConverterFactory.create(gson))

        val retrofit = builder.client(httpClient.build())?.build()
        return retrofit?.create(serviceClass)!!
    }
}