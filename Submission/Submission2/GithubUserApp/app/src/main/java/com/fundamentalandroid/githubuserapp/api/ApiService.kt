package com.fundamentalandroid.githubuserapp.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @Headers("Authorization: token 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")
    @GET("search/users")
    fun getSearch(
        @Query("q") username: String
    ): Call<Response>

    @Headers("Authorization: token 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")
    @GET("users/{username}")
    fun getUser(
        @Path("username") username: String
    ): Call<User>

    @Headers("Authorization: token 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")
    @GET("users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String
    ): Call<List<User>>

    @Headers("Authorization: token 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")
    @GET("users/{username}/following")
    fun getFollowing(
        @Path("username") username: String
    ): Call<List<User>>
}