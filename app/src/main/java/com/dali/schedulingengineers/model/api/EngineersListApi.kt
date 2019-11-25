package com.dali.schedulingengineers.model.api

import android.content.Context
import com.dali.schedulingengineers.BuildConfig
import com.dali.schedulingengineers.model.api.client.RestClient
import com.dali.schedulingengineers.model.models.Engineer
import com.dali.schedulingengineers.model.models.EngineersList
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

typealias EngineersListCallback = (String?, List<Engineer>?) -> Unit

class EngineersListApi(val context: Context) {

    private interface EngineersApi {
        @Headers(RestClient.HEADER_CACHE_CONTROL_NO_CACHE)
        @GET("engineersList")
        fun getEngineersList(): Call<EngineersList>
    }

    private val engineersApi by lazy {
        RestClient.getInstance(context)
            .createService(
                EngineersApi::class.java,
                GsonBuilder().serializeNulls().create(),
                BuildConfig.BACKEND_URL
            )
    }

    fun getEngineersList(callback: EngineersListCallback) {
        engineersApi.getEngineersList().enqueue(object :
            Callback<EngineersList> {
            override fun onFailure(call: Call<EngineersList>?, t: Throwable?) {
                t?.printStackTrace()
                callback(t?.message, null)
            }

            override fun onResponse(
                call: Call<EngineersList>?,
                response: Response<EngineersList>?
            ) {
                val errorBody = response?.errorBody()
                    ?.string()
                response?.let {
                    if (response.isSuccessful) {
                        response.body()?.let { responseBody ->
                            callback(null, responseBody.engineers) // SUCCESS
                        } ?: callback(null, null)
                    } else {
                        errorBody?.let { errorBodyString ->
                            callback(errorBodyString, null)
                        } ?: callback(null, null)
                    }
                } ?: callback(null, null)
            }
        })
    }
}