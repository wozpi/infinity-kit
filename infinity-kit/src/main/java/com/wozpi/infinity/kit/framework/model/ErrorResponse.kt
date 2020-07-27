package com.wozpi.infinity.kit.framework.model

data class ErrorResponse(
    val status:Int = -1,
    val message:String,
    val noNetwork:Boolean = false
){
    companion object{

        fun createExceptionInternal(message: String? = null) = ErrorResponse(500,message?: "Server is under maintenance, try again few minute later")

        fun commonError(message: String? = null) = ErrorResponse(-1,message?:"Unknown")

        fun noHaveNetwork() = ErrorResponse(-1,"Loses internet connection",true)

        fun requestTimeOut() = ErrorResponse(-1,"Looks like the server is taking to long respond, this can be caused by either poor " +
                "connectivity or an error with our servers. Please try again in a while.")
    }
}