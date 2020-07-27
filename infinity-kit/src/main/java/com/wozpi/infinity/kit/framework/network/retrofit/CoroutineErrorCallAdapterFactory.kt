package com.wozpi.infinity.kit.framework.network.retrofit

import com.wozpi.infinity.kit.framework.model.InfinityKitResponse
import com.wozpi.infinity.kit.framework.network.ConnectActivity
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.ClassCastException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class CoroutineErrorCallAdapterFactory(private val connectActivity: ConnectActivity) : CallAdapter.Factory() {

    companion object{
        fun create(connectActivity: ConnectActivity) = CoroutineErrorCallAdapterFactory(connectActivity)
    }

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        return try {
            val enclosingType = (returnType as ParameterizedType)
            if(enclosingType != InfinityKitResponse::class.java){
                null
            }else{
                val type = enclosingType.actualTypeArguments[0]
                CoroutineCallAdapter<Any>(type)
            }
        }catch (e: ClassCastException){
            e.printStackTrace()
            null
        }
    }

    inner class CoroutineCallAdapter<R>(private val responseType: Type) : CallAdapter<R,Any>{

        override fun adapt(call: Call<R>): Any = InfinityKitResponse(call,connectActivity)

        override fun responseType(): Type = responseType
    }
}