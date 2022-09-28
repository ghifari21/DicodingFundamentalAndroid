package com.fundamentalandroid.githubuserapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fundamentalandroid.githubuserapp.data.local.entity.UserEntity
import com.fundamentalandroid.githubuserapp.data.remote.response.User
import com.fundamentalandroid.githubuserapp.databinding.ItemUserBinding

class ListUserAdapter(private val listUser: List<UserEntity>) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val username = listUser[position].username
        val avatar = listUser[position].avatarUrl
        val id = "ID: ${listUser[position].id}"

        holder.apply {
            Glide.with(binding.root.context)
                .load(avatar)
                .circleCrop()
                .into(binding.imgItemAvatar)

            binding.tvItemUsername.text = username
            binding.tvItemId.text = id

            binding.root.setOnClickListener {
                onItemClickCallback.onItemClicked(listUser[adapterPosition].username)
            }
        }

    }

    override fun getItemCount(): Int = listUser.size

    class ListViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(username: String)
    }
}