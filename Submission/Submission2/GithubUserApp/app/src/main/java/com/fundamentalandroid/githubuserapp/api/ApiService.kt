package com.fundamentalandroid.githubuserapp.api

import com.fundamentalandroid.githubuserapp.BuildConfig
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("Authorization: token ${BuildConfig.KEY}")
    @GET("search/users")
    fun getSearch(
        @Query("q") username: String
    ): Call<Response>

    @Headers("Authorization: token ${BuildConfig.KEY}")
    @GET("users/{username}")
    fun getUser(
        @Path("username") username: String
    ): Call<User>

    @Headers("Authorization: token ${BuildConfig.KEY}")
    @GET("users/{username}/{type}")
    fun getFollow(
        @Path("username") username: String,
        @Path("type") type: String
    ): Call<List<User>>
}