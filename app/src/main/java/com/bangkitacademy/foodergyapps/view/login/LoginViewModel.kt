package com.bangkitacademy.foodergyapps.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkitacademy.foodergyapps.data.FoodRepository
import com.bangkitacademy.foodergyapps.data.response.LoginItem
import com.bangkitacademy.foodergyapps.data.response.LoginResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: FoodRepository): ViewModel(){

    val toastMessage: LiveData<String> = repository.toastMessage

    val isLoading: LiveData<Boolean> = repository.isLoading
    val userLogin: LiveData<LoginResult> = repository.userLogin
    fun loginUser(email:String, password: String) = repository.loginUser(email, password)
}