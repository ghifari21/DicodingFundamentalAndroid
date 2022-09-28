package com.fundamentalandroid.githubuserapp.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fundamentalandroid.githubuserapp.R
import com.fundamentalandroid.githubuserapp.data.local.entity.UserEntity
import com.fundamentalandroid.githubuserapp.databinding.ItemUserBinding
import com.fundamentalandroid.githubuserapp.ui.DetailActivity

class UserListAdapter : ListAdapter<UserEntity, UserListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class MyViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {
        fun bind(user: UserEntity) {
            binding.tvItemUsername.text = user.username
            binding.tvItemId.text =
                binding.itemUser.context.getString(R.string.id_account, user.id.toInt())
            Glide.with(itemView.context)
                .load(user.avatarUrl)
                .circleCrop()
                .into(binding.imgItemAvatar)

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DATA, user.username)
                itemView.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<UserEntity> =
            object : DiffUtil.ItemCallback<UserEntity>() {
                override fun areItemsTheSame(oldUser: UserEntity, newUser: UserEntity): Boolean {
                    return oldUser.username == newUser.username
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: UserEntity, newUser: UserEntity): Boolean {
                    return oldUser == newUser
                }
            }
    }
}