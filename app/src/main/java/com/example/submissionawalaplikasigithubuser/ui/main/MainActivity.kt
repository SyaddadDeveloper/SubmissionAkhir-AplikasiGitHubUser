package com.example.submissionawalaplikasigithubuser.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissionawalaplikasigithubuser.R
import com.example.submissionawalaplikasigithubuser.data.response.GithubResponse
import com.example.submissionawalaplikasigithubuser.data.response.ItemsItem
import com.example.submissionawalaplikasigithubuser.data.retrofit.ApiConfig
import com.example.submissionawalaplikasigithubuser.databinding.ActivityMainBinding
import com.example.submissionawalaplikasigithubuser.ui.favorite.FavoriteUserActivity
import com.example.submissionawalaplikasigithubuser.ui.setting.SettingThemeActivity
import com.example.submissionawalaplikasigithubuser.ui.setting.SettingThemePreferences
import com.example.submissionawalaplikasigithubuser.ui.setting.SettingThemeViewModel
import com.example.submissionawalaplikasigithubuser.ui.setting.SettingThemeViewModelFactory
import com.example.submissionawalaplikasigithubuser.ui.setting.dataStore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val userViewModel by viewModels<UserViewModel>()

    companion object {
        private const val TAG = "MainActivity"
        private const val USER_NAME = "Arif-un"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val pref = SettingThemePreferences.getInstance(application.dataStore)
        val settingViewModel =
            ViewModelProvider(this, SettingThemeViewModelFactory(pref))[SettingThemeViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchBar.inflateMenu(R.menu.option_menu)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
                    userViewModel.setReviewData(searchBar.text.toString())
                    userViewModel.listUser.observe(this@MainActivity) {
                        if (it.isNullOrEmpty()) {
                            showNotFound(true)
                        } else {
                            showNotFound(false)
                        }
                    }
                    false
                }
            searchBar.setOnMenuItemClickListener { menuitem ->
                when (menuitem.itemId) {
                    R.id.menu1 -> {
                        val intent = Intent(this@MainActivity, FavoriteUserActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.menu2 -> {
                        val intent = Intent(this@MainActivity, SettingThemeActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
        }

        userViewModel.listUser.observe(this) {
            if (it != null) {
                setReviewData(it)
            }
        }

        userViewModel.isLoading.observe(this) {
            showLoading(it)
        }


        val layoutManager = LinearLayoutManager(this)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)
        findUser()
    }

    private fun findUser() {
        showLoading(true)
        val client = ApiConfig.getApiService().getUser(USER_NAME)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>,
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setReviewData(responseBody.items)

                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setReviewData(item: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(item)
        binding.rvUsers.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showNotFound(isDataNotFound: Boolean) {
        binding.apply {
            if (isDataNotFound) {
                rvUsers.visibility = View.GONE
                errorMessage.visibility = View.VISIBLE
            } else {
                rvUsers.visibility = View.VISIBLE
                errorMessage.visibility = View.GONE
            }
        }
    }
}