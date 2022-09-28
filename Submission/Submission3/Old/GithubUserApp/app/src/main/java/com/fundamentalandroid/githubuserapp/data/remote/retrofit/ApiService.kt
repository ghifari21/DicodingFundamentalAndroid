package com.fundamentalandroid.githubuserapp.data.remote.retrofit

import com.fundamentalandroid.githubuserapp.BuildConfig
import com.fundamentalandroid.githubuserapp.data.remote.response.Response
import com.fundamentalandroid.githubuserapp.data.remote.response.User
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("Authorization: token ${BuildConfig.KEY}")
    @GET("search/users")
    suspend fun getSearch(
        @Query("q") username: String
    ): Response

    @Headers("Authorization: token ${BuildConfig.KEY}")
    @GET("users/{username}")
    suspend fun getUser(
        @Path("username") username: String
    ): User

    @Headers("Authorization: token ${BuildConfig.KEY}")
    @GET("users/{username}/{type}")
    suspend fun getFollow(
        @Path("username") username: String,
        @Path("type") type: String
    ): List<User>
}