package com.bangkitacademy.foodergyapps.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkitacademy.foodergyapps.data.UserPreference
import com.bangkitacademy.foodergyapps.data.model.UserSession
import com.bangkitacademy.foodergyapps.data.response.LoginItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DataStoreViewModel @Inject constructor(private val preference: UserPreference): ViewModel() {

    fun getSession(): LiveData<UserSession> {
        return preference.getUser().asLiveData()
    }

//    fun getSession2(): LiveData<LoginItem> {
//        return preference.getUser2().asLiveData()
//    }

    fun setSession(user: UserSession) {
        viewModelScope.launch {
            preference.setUser(user)
        }
    }

    fun setSession2(user: LoginItem) {
        viewModelScope.launch {
            preference.setUser2(user)
        }
    }

    fun logout() {
        viewModelScope.launch {
            preference.logout()
        }
    }
    fun logout2() {
        viewModelScope.launch {
            preference.logout2()
        }
    }
}