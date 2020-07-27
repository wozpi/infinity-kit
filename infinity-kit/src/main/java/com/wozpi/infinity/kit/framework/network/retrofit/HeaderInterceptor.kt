package com.wozpi.infinity.kit.framework.network.retrofit

import com.wozpi.infinity.kit.framework.database.InfinityKitPreferenceManager
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val infinityKitPreferenceManager: InfinityKitPreferenceManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestChain = chain.request().newBuilder()
            .addHeader("Content-Type","application/json")
        infinityKitPreferenceManager.getToken()?.apply {
            requestChain.addHeader("Authorization","Bearer $this")
        }
        val request = requestChain.build()
        return chain.proceed(request)
    }
}