package com.fundamentalandroid.githubuserapp.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fundamentalandroid.githubuserapp.R
import com.fundamentalandroid.githubuserapp.data.Result
import com.fundamentalandroid.githubuserapp.databinding.ActivityMainBinding
import com.fundamentalandroid.githubuserapp.ui.adapter.UserListAdapter
import com.fundamentalandroid.githubuserapp.ui.viewmodel.SettingViewModel
import com.fundamentalandroid.githubuserapp.ui.viewmodel.UserViewModel
import com.fundamentalandroid.githubuserapp.ui.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: UserViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private val settingViewModel: SettingViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    private val userAdapter = UserListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        settingViewModel.getSetting().observe(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        installSplashScreen()

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = userAdapter
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        binding.svSearch.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    clearFocus()
                    binding.tvOnboard.visibility = View.INVISIBLE
                    binding.rvUser.visibility = View.INVISIBLE
                    setSearchData(query!!)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }

    private fun setSearchData(query: String) {
        if (query.isNotEmpty()) {
            viewModel.getSearch(query).observe(this@MainActivity) {
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
                            userAdapter.submitList(it.data)
                        }
                        is Result.Error -> {
                            binding.apply {
                                progressBar.visibility = View.INVISIBLE
                                tvSearching.visibility = View.INVISIBLE
                                rvUser.visibility = View.INVISIBLE
                            }
                            Toast.makeText(
                                this,
                                "Terjadi kesalahan ${it.error}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite_menu -> {
                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.setting_menu -> {
                val intent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }
}