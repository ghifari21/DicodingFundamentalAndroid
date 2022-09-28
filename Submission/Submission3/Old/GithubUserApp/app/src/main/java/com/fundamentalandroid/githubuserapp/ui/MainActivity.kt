package com.fundamentalandroid.githubuserapp.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fundamentalandroid.githubuserapp.data.remote.response.User
import com.fundamentalandroid.githubuserapp.databinding.ActivityMainBinding
import com.fundamentalandroid.githubuserapp.ui.adapter.ListUserAdapter
import com.fundamentalandroid.githubuserapp.ui.viewmodel.UserViewModel
import com.fundamentalandroid.githubuserapp.ui.viewmodel.ViewModelFactory
import com.fundamentalandroid.githubuserapp.data.Result
import com.fundamentalandroid.githubuserapp.data.local.entity.UserEntity
//import com.fundamentalandroid.githubuserapp.ui.viewmodelold.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: UserViewModel by viewModels {
            factory
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding.svSearch.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    binding.tvOnboard.visibility = View.INVISIBLE
                    if (query?.isNotEmpty() == true) {
                        viewModel.getUserList(query).observe(this@MainActivity) {
                            if (it != null) {
                                when (it) {
                                    is Result.Loading -> {
                                        binding.apply {
                                            progressBar.visibility = View.VISIBLE
                                            tvSearching.visibility = View.VISIBLE
                                            rvUser.visibility = View.INVISIBLE
                                        }
                                    }
                                    is Result.Success -> {
                                        binding.apply {
                                            progressBar.visibility = View.INVISIBLE
                                            tvSearching.visibility = View.INVISIBLE
                                            rvUser.visibility = View.VISIBLE
                                        }
                                        setSearchData(it.data)
                                    }
                                    else -> {

                                    }
                                }
                            }
                        }
                    }
                    clearFocus()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }

//        mainViewModel.listSearch.observe(this) {
//            setSearchData(it)
//        }
//
//        mainViewModel.isLoading.observe(this) {
//            showLoading(it)
//        }
//
//        mainViewModel.snackbar.observe(this) {
//            it.getContentIfNotHandled()?.let { text ->
//                Snackbar.make(
//                    window.decorView.rootView,
//                    text,
//                    Snackbar.LENGTH_SHORT
//                ).show()
//            }
//        }
    }

    private fun setSearchData(users: List<UserEntity>) {
        val adapter = ListUserAdapter(users)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(username: String) {
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DATA, username)
                startActivity(intent)
            }
        })
    }

//    private fun showLoading(isLoading: Boolean) {
//        binding.apply {
//            if (isLoading) {
//                progressBar.visibility = View.VISIBLE
//                tvSearching.visibility = View.VISIBLE
//                rvUser.visibility = View.INVISIBLE
//            } else {
//                progressBar.visibility = View.INVISIBLE
//                tvSearching.visibility = View.INVISIBLE
//                rvUser.visibility = View.VISIBLE
//            }
//        }
//    }
}