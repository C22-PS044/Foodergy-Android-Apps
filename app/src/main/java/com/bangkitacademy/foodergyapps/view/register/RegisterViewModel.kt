package com.bangkitacademy.foodergyapps.view.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.bangkitacademy.foodergyapps.data.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject


@HiltViewModel
class RegisterViewModel  @Inject constructor(private val repository: FoodRepository): ViewModel() {

    val isLoading: LiveData<Boolean> = repository.isLoading

    fun registerUser(name: String, email: String, password: String, alergi: String){
        repository.registerUser(name, email, password, alergi)
    }
}