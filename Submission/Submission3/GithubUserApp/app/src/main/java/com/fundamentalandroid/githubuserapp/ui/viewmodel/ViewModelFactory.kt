package com.fundamentalandroid.githubuserapp.ui.viewmodel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fundamentalandroid.githubuserapp.data.UserRepository
import com.fundamentalandroid.githubuserapp.di.Injection

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class ViewModelFactory private constructor(private val userRepository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(userRepository) as T
        } else if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(
                        context,
                        context.dataStore
                    )
                )
            }.also {
                instance = it
            }
    }
}