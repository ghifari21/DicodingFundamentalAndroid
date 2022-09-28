package com.fundamentalandroid.githubuserapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.fundamentalandroid.githubuserapp.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE username = :username ORDER BY id ASC")
    fun getUser(username: String): LiveData<List<UserEntity>>

    @Query("SELECT * FROM user WHERE isFavorite = 1")
    fun getFavoriteUser(): LiveData<List<UserEntity>>

    @Insert
    suspend fun insertUser(user: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("DELETE FROM user WHERE isFavorite = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM user WHERE username = :username AND isFavorite = 1)")
    suspend fun isUserFavorite(username: String): Boolean
}