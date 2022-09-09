package com.fundamentalandroid.githubuserapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.fundamentalandroid.githubuserapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<User>(EXTRA_DATA) as User
        val followers = "${data.followers} Followers"
        val following = "${data.following} Following"

        supportActionBar?.title = "${data.name}'s Profile"

        with(binding) {
            Glide.with(root.context)
                .load(data.avatar)
                .circleCrop()
                .into(imgAvatar)

            tvName.text = data.name
            tvUsername.text = data.username
            tvFollowers.text = followers
            tvFollowing.text = following
            tvCompany.text = data.company
            tvLocation.text = data.location
            tvRepository.text = data.repository.toString()

            followBtn.setOnClickListener {
                followButtonToast(data.name)
            }

            shareBtn.setOnClickListener {
                val user = "Username: ${data.username}\n" +
                        "Name: ${data.name}\n" +
                        "Location: ${data.location}\n" +
                        "Company: ${data.company}\n" +
                        "${data.repository} Repository\n" +
                        "${data.followers} Followers\n" +
                        "${data.following} Following"

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, user)
                startActivity(Intent.createChooser(intent, "Share via"))
            }
        }
    }

    private fun followButtonToast(name: String) {
        Toast.makeText(this, "You just followed $name", Toast.LENGTH_SHORT).show()
    }
}