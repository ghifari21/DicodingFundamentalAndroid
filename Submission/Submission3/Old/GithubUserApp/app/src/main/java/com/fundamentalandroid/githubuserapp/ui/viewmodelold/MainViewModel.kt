package com.fundamentalandroid.githubuserapp.ui.viewmodelold

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fundamentalandroid.githubuserapp.data.remote.retrofit.ApiConfig
import com.fundamentalandroid.githubuserapp.data.remote.response.Response
import com.fundamentalandroid.githubuserapp.data.remote.response.User
import com.fundamentalandroid.githubuserapp.utils.SingleEvent
import retrofit2.Call
import retrofit2.Callback

class MainViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _listSearch = MutableLiveData<List<User>>()
    val listSearch: LiveData<List<User>> = _listSearch

    private val _snackbar = MutableLiveData<SingleEvent<String>>()
    val snackbar: LiveData<SingleEvent<String>> = _snackbar

    fun findUser(username: String) {
        _isLoading.value = true

        val client = ApiConfig.getApiService().getSearch(username)
        client.enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                _isLoading.value = false

                if (response.isSuccessful && response.body() != null) {
                    val responseBody = response.body()
                    _listSearch.value = responseBody?.items
                    if (responseBody?.totalCount == 0) {
                        _snackbar.value = SingleEvent("User not found!")
                    }
                } else {
                    _snackbar.value = SingleEvent(response.message())
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                _isLoading.value = false
                _snackbar.value = SingleEvent(t.message.toString())
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}