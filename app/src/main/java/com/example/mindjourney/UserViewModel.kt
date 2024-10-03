package com.example.mindjourney

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mindjourney.screens.User

class UserViewModel : ViewModel() {
    val currentUser = MutableLiveData<User?>()

    // Function to update the user when they log in
    fun login(user: User) {
        currentUser.value = user
    }

    // Function to clear the user when they log out
    fun logout() {
        currentUser.value = null
    }
}
