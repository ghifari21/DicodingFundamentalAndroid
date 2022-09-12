package com.fundamentalandroid.githubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fundamentalandroid.githubuserapp.api.ApiConfig
import com.fundamentalandroid.githubuserapp.api.User
import com.fundamentalandroid.githubuserapp.event.SingleEvent
import retrofit2.Call
import retrofit2.Callback

class TabsViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listUser = MutableLiveData<List<User>>()
    val listUser: LiveData<List<User>> = _listUser

    private val _snackbar = MutableLiveData<SingleEvent<String>>()
    val snackbar: LiveData<SingleEvent<String>> = _snackbar

    fun findUsers(username: String, index: Int) {
        _isLoading.value = true

        val client = if (index == 0) {
            ApiConfig.getApiService().getFollow(username, "followers")
        } else {
            ApiConfig.getApiService().getFollow(username, "following")
        }

        client.enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: retrofit2.Response<List<User>>) {
                _isLoading.value = false

                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()
                    _listUser.value = responseBody!!
                } else {
                    _snackbar.value = SingleEvent(response.message())
                    Log.e(TAG, "onFailure not success: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                _isLoading.value = false
                _snackbar.value = SingleEvent(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "TabsViewModel"
    }
}