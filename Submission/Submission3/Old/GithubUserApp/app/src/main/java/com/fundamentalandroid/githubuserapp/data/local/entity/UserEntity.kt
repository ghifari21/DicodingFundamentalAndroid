package com.fundamentalandroid.githubuserapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
class UserEntity(
    @field:ColumnInfo(name = "id")
    @PrimaryKey
    val id: String,

    @field:ColumnInfo(name = "username")
    val username: String,

    @field:ColumnInfo(name = "name")
    val name: String? = null,

    @field:ColumnInfo(name = "avatarUrl")
    val avatarUrl: String,

    @field:ColumnInfo(name = "url")
    val url: String,

    @field:ColumnInfo(name = "company")
    val company: String? = null,

    @field:ColumnInfo(name = "location")
    val location: String? = null,

    @field:ColumnInfo(name = "publicRepos")
    val publicRepos: Int? = null,

    @field:ColumnInfo(name = "followers")
    val followers: Int? = null,

    @field:ColumnInfo(name = "following")
    val following: Int? = null,

    @field:ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean
)