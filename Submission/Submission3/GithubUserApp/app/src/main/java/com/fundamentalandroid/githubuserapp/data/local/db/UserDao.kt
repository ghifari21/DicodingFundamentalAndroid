package com.fundamentalandroid.githubuserapp.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.fundamentalandroid.githubuserapp.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE isFavorite = 1 ORDER BY username ASC")
    fun getUserFavorite(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM user WHERE username = :username LIMIT 1")
    fun getUserDetail(username: String): LiveData<UserEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("DELETE FROM user WHERE isFavorite = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM user WHERE username = :username AND isFavorite = 1)")
    suspend fun isUserFavorite(username: String): Boolean
}