package com.mubarak.android_unittest_mockwebserver.utils

data class Resource<out T>(val status: Status, val responseData: T?, val message: String?) {

    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

    }

}

sealed class ResponseHandler<out T> {
    object Loading : ResponseHandler<Nothing>()
    class OnFailed(val code:Int,val message:String, val messageCode:String) : ResponseHandler<Nothing>()
    class OnSuccessResponse<T>(val response: T) : ResponseHandler<T>()
}