package com.fundamentalandroid.githubuserapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import com.bumptech.glide.Glide
import com.fundamentalandroid.githubuserapp.R
import com.fundamentalandroid.githubuserapp.data.Result
import com.fundamentalandroid.githubuserapp.data.local.entity.UserEntity
import com.fundamentalandroid.githubuserapp.databinding.ActivityDetailBinding
import com.fundamentalandroid.githubuserapp.ui.adapter.SectionsPagerAdapter
import com.fundamentalandroid.githubuserapp.ui.viewmodel.UserViewModel
import com.fundamentalandroid.githubuserapp.ui.viewmodel.ViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var link: String
    private lateinit var user: UserEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.elevation = 0f

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
        val viewModel: UserViewModel by viewModels {
            factory
        }

        val username = intent.getStringExtra(EXTRA_DATA)
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        viewModel.getUserDetail(username!!).observe(this) {
            if (it != null) {
                when (it) {
                    is Result.Loading -> {
                        binding.apply {
                            progressLoading.visibility = View.VISIBLE
                            tvLoading.visibility = View.VISIBLE
                        }
                    }
                    is Result.Success -> {
                        supportActionBar?.title = "${it.data.name ?: it.data.username}'s Profile"
                        user = it.data

                        binding.apply {
                            progressLoading.visibility = View.INVISIBLE
                            tvLoading.visibility = View.INVISIBLE

                            Glide.with(root.context)
                                .load(it.data.avatarUrl)
                                .circleCrop()
                                .into(imgAvatar)
                            tvName.text = it.data.name ?: it.data.username
                            tvUsername.text = it.data.username
                            tvFollowers.text =
                                resources.getString(R.string.followers, it.data.followers)
                            tvFollowing.text =
                                resources.getString(R.string.following, it.data.following)
                            tvCompany.text = it.data.company ?: resources.getString(R.string.data_null)
                            tvLocation.text = it.data.location ?: resources.getString(R.string.data_null)
                            tvRepository.text =
                                resources.getString(R.string.pub_repos, it.data.publicRepos)

                            link = it.data.url

                            val icon = if (it.data.isFavorite) {
                                AppCompatResources.getDrawable(this@DetailActivity, R.drawable.ic_baseline_favorite_red_24)
                            } else {
                                AppCompatResources.getDrawable(this@DetailActivity, R.drawable.ic_baseline_favorite_border_24)
                            }
                            binding.favoriteBtn.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null)
                        }
                    }
                    is Result.Error -> {
                        binding.apply {
                            progressLoading.visibility = View.INVISIBLE
                            tvLoading.visibility = View.INVISIBLE
                        }
                        Toast.makeText(
                            this,
                            "Terjadi kesalahan " + it.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.apply {
            favoriteBtn.setOnClickListener {
                viewModel.setUserFavorite(user)
            }
            shareBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, link)
                startActivity(Intent.createChooser(intent, "Share via"))
            }
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
                val intent = Intent(this@DetailActivity, SettingActivity::class.java)
                startActivity(intent)
                true
            }
            else -> true
        }
    }

    companion object {
        const val EXTRA_DATA = "extra_data"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.title_followers,
            R.string.title_following
        )
    }
}