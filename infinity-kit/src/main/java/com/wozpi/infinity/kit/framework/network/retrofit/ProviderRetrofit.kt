package com.wozpi.infinity.kit.framework.network.retrofit

import com.google.gson.GsonBuilder
import com.wozpi.infinity.kit.BuildConfig
import com.wozpi.infinity.kit.framework.database.InfinityKitPreferenceManager
import com.wozpi.infinity.kit.framework.network.ConnectActivity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

open class ProviderRetrofit(infinityKitPreferenceManager: InfinityKitPreferenceManager, connectActivity: ConnectActivity) {
    companion object {
        const val TIME_OUT = 60L
    }
    var mRetrofit: Retrofit

    init {
        /**
         * Show logging retrofit
         * */
        val interceptor = HttpLoggingInterceptor()

        interceptor.level = if(buildConfig()){
            HttpLoggingInterceptor.Level.BODY
        }else{
            HttpLoggingInterceptor.Level.NONE
        }
        val gson = GsonBuilder()
            .enableComplexMapKeySerialization()
            .serializeNulls()
            .setPrettyPrinting()
            .create()

        val clientWithRetrofit = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(HeaderInterceptor(infinityKitPreferenceManager))
            .connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
            .readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
            .build()

        mRetrofit = Retrofit.Builder()
            .baseUrl(getBaseUrl())
            .client(clientWithRetrofit)
            .addCallAdapterFactory(CoroutineErrorCallAdapterFactory.create(connectActivity))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun buildConfig():Boolean = BuildConfig.DEBUG

    fun getBaseUrl():String = ""

    inline fun <reified T> createNetworkService():T = mRetrofit.create(T::class.java)
}