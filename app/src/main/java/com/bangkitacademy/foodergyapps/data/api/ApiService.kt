package com.bangkitacademy.foodergyapps.data.api

import com.bangkitacademy.foodergyapps.data.response.LoginResponse
import com.bangkitacademy.foodergyapps.data.response.LoginResponse2
import com.bangkitacademy.foodergyapps.response.RegisterResponse
import com.bangkitacademy.foodergyapps.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun createUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("alergi") alergi: String
    ): Call<RegisterResponse>

//    @FormUrlEncoded
//    @POST("login")
//    fun loginUser(
//        @Field("email") email: String,
//        @Field("password") password: String
//    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>
}