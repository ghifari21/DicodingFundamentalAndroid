package com.fundamentalandroid.githubuserapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.fundamentalandroid.githubuserapp.data.UserRepository
import com.fundamentalandroid.githubuserapp.data.local.datastore.SettingPreferences
import com.fundamentalandroid.githubuserapp.data.local.db.UserDatabase
import com.fundamentalandroid.githubuserapp.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context, dataStore: DataStore<Preferences>): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = UserDatabase.getInstance(context)
        val dao = database.userDao()
        val pref = SettingPreferences.getInstance(dataStore)
        return UserRepository.getInstance(apiService, dao, pref)
    }
}