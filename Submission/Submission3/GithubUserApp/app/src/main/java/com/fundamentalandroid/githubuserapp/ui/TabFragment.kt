package com.fundamentalandroid.githubuserapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.fundamentalandroid.githubuserapp.R
import com.fundamentalandroid.githubuserapp.data.Result
import com.fundamentalandroid.githubuserapp.databinding.FragmentTabBinding
import com.fundamentalandroid.githubuserapp.ui.adapter.UserListAdapter
import com.fundamentalandroid.githubuserapp.ui.viewmodel.UserViewModel
import com.fundamentalandroid.githubuserapp.ui.viewmodel.ViewModelFactory

class TabFragment : Fragment() {
    private lateinit var tvLoading: TextView
    private lateinit var progressBar: ProgressBar
    private var _binding: FragmentTabBinding? = null
    private val binding get() = _binding
    private var tabName: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTabBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvLoading = requireActivity().findViewById(R.id.tv_loading)
        progressBar = requireActivity().findViewById(R.id.progress_loading)

        tabName = arguments?.getString(ARG_TAB)
        val username = arguments?.getString(ARG_USERNAME)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val viewModel: UserViewModel by viewModels {
            factory
        }

        val userAdapter = UserListAdapter()

        val layoutManager = LinearLayoutManager(view.context)
        binding?.rvUser?.layoutManager = layoutManager
        binding?.rvUser?.setHasFixedSize(true)
        val itemDecoration = DividerItemDecoration(view.context, layoutManager.orientation)
        binding?.rvUser?.addItemDecoration(itemDecoration)
        binding?.rvUser?.adapter = userAdapter

        viewModel.getFollow(username!!, tabName!!).observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is Result.Loading -> {
                        tvLoading.visibility = View.VISIBLE
                        progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        tvLoading.visibility = View.INVISIBLE
                        progressBar.visibility = View.INVISIBLE
                        userAdapter.submitList(it.data)
                    }
                    is Result.Error -> {
                        tvLoading.visibility = View.INVISIBLE
                        progressBar.visibility = View.INVISIBLE
                        Toast.makeText(
                            context,
                            "Terjadi kesalahan " + it.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val ARG_TAB = "tab_name"
        const val ARG_USERNAME = "username"
        const val TAB_FOLLOWERS = "followers"
        const val TAB_FOLLOWING = "following"
    }
}