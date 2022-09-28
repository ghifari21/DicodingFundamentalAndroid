package com.fundamentalandroid.githubuserapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fundamentalandroid.githubuserapp.R
import com.fundamentalandroid.githubuserapp.databinding.ActivityFavoriteBinding
import com.fundamentalandroid.githubuserapp.ui.adapter.UserListAdapter
import com.fundamentalandroid.githubuserapp.ui.viewmodel.UserViewModel
import com.fundamentalandroid.githubuserapp.ui.viewmodel.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "User's Favorite"

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: UserViewModel by viewModels {
            factory
        }

        val userAdapter = UserListAdapter()
        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.adapter = userAdapter
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        viewModel.getUserFavorite().observe(this) {
            binding.rvUser.visibility = View.VISIBLE
            userAdapter.submitList(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.setting_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting_menu -> {
                val intent = Intent(this@FavoriteActivity, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }
}