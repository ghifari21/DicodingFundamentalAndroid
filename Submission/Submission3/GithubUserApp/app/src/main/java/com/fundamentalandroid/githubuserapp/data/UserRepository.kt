package com.fundamentalandroid.githubuserapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.fundamentalandroid.githubuserapp.data.local.datastore.SettingPreferences
import com.fundamentalandroid.githubuserapp.data.local.db.UserDao
import com.fundamentalandroid.githubuserapp.data.local.entity.UserEntity
import com.fundamentalandroid.githubuserapp.data.remote.retrofit.ApiService

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val pref: SettingPreferences
) {
    fun getSearch(query: String): LiveData<Result<List<UserEntity>>> = liveData {
        val userList = MutableLiveData<List<UserEntity>>()
        emit(Result.Loading)
        try {
            val response = apiService.getSearch(query)
            val users = response.items
            if (response.totalCount != 0) {
                val list = users.map {
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
                        it.following
                    )
                }
                userList.value = list
            } else {
                emit(Result.Error("user tidak ditemukan"))
            }
            userDao.deleteAll()
        } catch (e: Exception) {
            Log.d("UserRepository", "getSearch: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val data: LiveData<Result<List<UserEntity>>> = userList.map {
            Result.Success(it!!)
        }
        emitSource(data)
    }

    fun getUserFavorite(): LiveData<List<UserEntity>> = userDao.getUserFavorite()

    fun getUserDetail(username: String): LiveData<Result<UserEntity>> = liveData {
        emit(Result.Loading)
        try {
            if (!userDao.isUserFavorite(username)) {
                val response = apiService.getUser(username)
                val isFavorite = userDao.isUserFavorite(username)
                val user = UserEntity(
                    response.id,
                    response.login,
                    response.name,
                    response.avatarUrl,
                    response.htmlUrl,
                    response.company,
                    response.location,
                    response.publicRepos,
                    response.followers,
                    response.following,
                    isFavorite
                )
                userDao.insertUser(user)
            }
        } catch (e: Exception) {
            Log.d("UserRepository", "getUserDetail: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<UserEntity>> = userDao.getUserDetail(username).map {
            Result.Success(it)
        }
        emitSource(localData)
    }

    fun getFollowData(username: String, type: String): LiveData<Result<List<UserEntity>>> = liveData {
        val users = MutableLiveData<List<UserEntity>>()
        emit(Result.Loading)
        try {
            val response = apiService.getFollow(username, type)
            if (response.isNotEmpty()) {
                val userList = response.map {
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
                        it.following
                    )
                }
                users.value = userList
            }
        } catch (e: Exception) {
            Log.d("UserRepository", "getFollowData: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val data: LiveData<Result<List<UserEntity>>> = users.map {
            Result.Success(it!!)
        }
        emitSource(data)
    }

    suspend fun setUserFavorite(user: UserEntity) {
        user.isFavorite = !user.isFavorite
        userDao.updateUser(user)
    }

    fun getTheme() = pref.getThemeSetting()

    suspend fun saveTheme(isDarkModeActive: Boolean) {
        pref.saveThemeSetting(isDarkModeActive)
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            userDao: UserDao,
            pref: SettingPreferences
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(apiService, userDao, pref)
            }.also {
                instance = it
            }
    }
}