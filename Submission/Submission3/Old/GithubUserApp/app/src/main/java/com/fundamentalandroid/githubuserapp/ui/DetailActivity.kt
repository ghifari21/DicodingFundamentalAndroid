package com.fundamentalandroid.githubuserapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.fundamentalandroid.githubuserapp.R
import com.fundamentalandroid.githubuserapp.databinding.ActivityDetailBinding
import com.fundamentalandroid.githubuserapp.ui.adapter.SectionsPagerAdapter
import com.fundamentalandroid.githubuserapp.ui.viewmodelold.DetailViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var name: String
    private lateinit var link: String
    private val detailViewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.elevation = 0f

        val username = intent.getStringExtra(EXTRA_DATA)
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = username
        binding.viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        detailViewModel.getUser(username!!)

        detailViewModel.user.observe(this) {
            if (it.name != null) {
                supportActionBar?.title = "${it.name}'s Profile"
            } else {
                supportActionBar?.title = "${it.login}'s Profile"
            }

            binding.apply {
                Glide.with(root.context)
                    .load(it.avatarUrl)
                    .circleCrop()
                    .into(imgAvatar)

                if (it.name != null) {
                    tvName.text = it.name
                    name = it.name
                } else {
                    tvName.text = it.login
                    name = it.login
                }
                tvUsername.text = it.login
                tvFollowers.text = resources.getString(R.string.followers, it.followers)
                tvFollowing.text = resources.getString(R.string.following, it.following)
                if (it.company != null) {
                    tvCompany.text = it.company
                } else {
                    tvCompany.text = resources.getString(R.string.data_null)
                }
                if (it.location != null) {
                    tvLocation.text = it.location
                } else {
                    tvLocation.text = resources.getString(R.string.data_null)
                }
                tvRepository.text = resources.getString(R.string.pub_repos, it.publicRepos)

                link = it.htmlUrl
            }
        }

        detailViewModel.snackbar.observe(this) {
            it.getContentIfNotHandled()?.let { text ->
                Snackbar.make(
                    window.decorView.rootView,
                    text,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        binding.apply {
            followBtn.setOnClickListener {
                followButtonToast()
            }

            shareBtn.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, link)
                startActivity(Intent.createChooser(intent, "Share via"))
            }
        }
    }

    private fun followButtonToast() {
        Toast.makeText(this, "You just followed $name", Toast.LENGTH_SHORT).show()
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