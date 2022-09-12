package com.fundamentalandroid.githubuserapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fundamentalandroid.githubuserapp.R
import com.fundamentalandroid.githubuserapp.api.User
import com.fundamentalandroid.githubuserapp.databinding.FragmentTabsBinding
import com.fundamentalandroid.githubuserapp.ui.DetailActivity
import com.fundamentalandroid.githubuserapp.ui.adapter.ListUserAdapter
import com.fundamentalandroid.githubuserapp.viewmodel.TabsViewModel
import com.google.android.material.snackbar.Snackbar

class TabsFragment : Fragment() {
    private var _binding: FragmentTabsBinding? = null
    private val binding get() = _binding!!
    private val tabsViewModel by viewModels<TabsViewModel>()
    private lateinit var tvLoading: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTabsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvLoading = requireActivity().findViewById(R.id.tv_loading)
        progressBar = requireActivity().findViewById(R.id.progress_loading)

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = arguments?.getString(ARG_USERNAME)

        val layoutManager = LinearLayoutManager(view.context)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(view.context, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        tabsViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        tabsViewModel.findUsers(username!!, index!!)

        tabsViewModel.listUser.observe(viewLifecycleOwner) {
            setFollowData(it)
            Log.d("TEST", "onViewCreated: ${it.size}")
        }

        tabsViewModel.snackbar.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { text ->
                Snackbar.make(
                    requireActivity().window.decorView.rootView,
                    text,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setFollowData(users: List<User>) {
        val adapter = ListUserAdapter(users)
        binding.rvUser.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(username: String) {
                val intent = Intent(requireActivity(), DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DATA, username)
                startActivity(intent)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            tvLoading.visibility = View.VISIBLE
            progressBar.visibility = View.VISIBLE
        } else {
            tvLoading.visibility = View.INVISIBLE
            progressBar.visibility = View.INVISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "username"
    }
}