package com.example.jaankari.util

sealed class Resource<T>(
    val data:T?=null,
    val message:String?=null
){
    class Success<T>(date:T):Resource<T>(date)
    class Error<T>(message: String, data:T?=null):Resource<T>(data, message)
    class Loading<T>:Resource<T>()
}