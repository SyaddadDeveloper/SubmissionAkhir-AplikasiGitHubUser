package com.example.submissionawalaplikasigithubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.submissionawalaplikasigithubuser.data.database.FavoriteUser
import com.example.submissionawalaplikasigithubuser.data.repository.UserRepository

class FavoriteUserViewModel(application: Application) : ViewModel() {
    private val userRepository: UserRepository = UserRepository(application)
    private fun getAllFavoriteUsers(): LiveData<List<FavoriteUser>> = userRepository.getAllFavorite()
    init {
        getAllFavoriteUsers()
    }
}