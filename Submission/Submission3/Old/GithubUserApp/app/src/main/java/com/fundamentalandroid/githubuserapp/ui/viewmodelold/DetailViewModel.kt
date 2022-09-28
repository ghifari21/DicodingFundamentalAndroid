package com.fundamentalandroid.githubuserapp.ui.viewmodelold

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fundamentalandroid.githubuserapp.data.remote.retrofit.ApiConfig
import com.fundamentalandroid.githubuserapp.data.remote.response.User
import com.fundamentalandroid.githubuserapp.utils.SingleEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {
    private val _snackbar = MutableLiveData<SingleEvent<String>>()
    val snackbar: LiveData<SingleEvent<String>> = _snackbar

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun getUser(username: String) {
        val client = ApiConfig.getApiService().getUser(username)
        client.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()
                    _user.value = responseBody!!
                } else {
                    _snackbar.value = SingleEvent(response.message())
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                _snackbar.value = SingleEvent(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}