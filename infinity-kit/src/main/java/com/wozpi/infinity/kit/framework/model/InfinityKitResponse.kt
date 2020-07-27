package com.wozpi.infinity.kit.framework.model

import com.google.gson.Gson
import com.wozpi.infinity.kit.framework.network.ConnectActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class InfinityKitResponse<R>(private val call: Call<R>, private val connectActivity: ConnectActivity)  {

    fun run(onSuccess:(R)->Unit, onError: (ErrorResponse)->Unit){
        if(connectActivity.isHaveNetworkConnected()){
            try {
                call.enqueue(object : Callback<R> {
                    override fun onFailure(call: Call<R>, t: Throwable) {
                        onError(ErrorResponse.commonError(t.localizedMessage))
                    }

                    override fun onResponse(call: Call<R>, response: Response<R>) {
                        handleResponse(response,onSuccess,onError)
                    }

                })
            }catch (t: IOException){
                return if(t is SocketTimeoutException){
                    onError(ErrorResponse.requestTimeOut())
                }else{
                    onError(ErrorResponse.createExceptionInternal(t.localizedMessage))
                }
            }
        }else{
            onError(ErrorResponse.noHaveNetwork())
        }
    }

    private fun handleResponse(response: Response<R>, onSuccess: (R) -> Unit, onError: (ErrorResponse) -> Unit){
        if(response.isSuccessful){
            onSuccess(response.body()!!)
        }else{
            if(response.code() in 400..511){
                if(response.errorBody() != null){
                    val gson = Gson()
                    val errorResponse:ErrorResponse = try {
                        val jsonError = response.errorBody()!!.string()
                        val json = JSONObject(jsonError)
                        if(json.has(ErrorResponse::message.name)){
                            gson.fromJson(jsonError,ErrorResponse::class.java)
                        }else{
                            val message = json.get("error") as String
                            ErrorResponse.commonError(message)
                        }
                    }catch (e:Exception){
                        ErrorResponse.createExceptionInternal()
                    }

                    onError(errorResponse)
                }
            }else{
                onSuccess(response.body()!!)
            }
        }
    }
}