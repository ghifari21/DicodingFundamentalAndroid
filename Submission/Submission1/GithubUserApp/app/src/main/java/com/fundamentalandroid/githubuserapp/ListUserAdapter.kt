package com.fundamentalandroid.githubuserapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fundamentalandroid.githubuserapp.databinding.ItemUserBinding

class ListUserAdapter(private val listUser: ArrayList<User>) :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
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
        val name = listUser[position].name
        val location = listUser[position].location
        val avatar = listUser[position].avatar

        with(holder) {
            Glide.with(binding.root.context)
                .load(avatar)
                .circleCrop()
                .into(binding.imgItemAvatar)

            binding.tvItemUsername.text = username
            binding.tvItemName.text = name
            binding.tvItemLocation.text = location

            binding.root.setOnClickListener {
                onItemClickCallback.onItemClicked(listUser[adapterPosition])
            }

            binding.shareBtn.setOnClickListener {
                onItemClickCallback.onShareBtnClicked(listUser[adapterPosition])
            }

            binding.followBtn.setOnClickListener {
                onItemClickCallback.onFollowBtnClicked(listUser[adapterPosition].name)
            }
        }
    }

    override fun getItemCount(): Int = listUser.size

    class ListViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
        fun onShareBtnClicked(data: User)
        fun onFollowBtnClicked(name: String)
    }
}