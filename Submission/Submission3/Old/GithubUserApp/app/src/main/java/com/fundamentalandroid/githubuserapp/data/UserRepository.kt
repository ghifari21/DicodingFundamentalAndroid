package com.fundamentalandroid.githubuserapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.fundamentalandroid.githubuserapp.data.local.entity.UserEntity
import com.fundamentalandroid.githubuserapp.data.local.room.UserDao
import com.fundamentalandroid.githubuserapp.data.remote.retrofit.ApiService

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao
) {
    fun getUserList(username: String): LiveData<Result<List<UserEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getSearch(username)
            val users = response.items
            val userList = users.map {
                val isFavorite = userDao.isUserFavorite(it.login)
                UserEntity(
                    it.id,
                    it.login,
                    it.name,
                    it.avatarUrl,
                    it.htmlUrl,
                    it.company,
                    it.location,
                    it.publicRepos,
                    it.followers,
                    it.following,
                    isFavorite
                )
            }
            userDao.deleteAll()
            userDao.insertUser(userList)
        } catch (e: Exception) {
            Log.d("UserRepository", "getUser: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<UserEntity>>> = userDao.getUser(username).map {
            Result.Success(it)
        }
        emitSource(localData)
    }

    fun getUserFavorite(): LiveData<List<UserEntity>> = userDao.getFavoriteUser()

    suspend fun setUserFavorite(user: UserEntity, favoriteState: Boolean) {
        user.isFavorite = favoriteState
        userDao.updateUser(user)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userDao: UserDao
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userDao)
            }.also {
                instance = it
            }
    }
}