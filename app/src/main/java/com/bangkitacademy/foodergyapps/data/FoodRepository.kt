package com.bangkitacademy.foodergyapps.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bangkitacademy.foodergyapps.data.api.ApiService
import com.bangkitacademy.foodergyapps.data.response.LoginItem
import com.bangkitacademy.foodergyapps.data.response.LoginResponse
import com.bangkitacademy.foodergyapps.data.response.LoginResponse2
import com.bangkitacademy.foodergyapps.data.response.LoginResult
import com.bangkitacademy.foodergyapps.response.RegisterItem
import com.bangkitacademy.foodergyapps.response.RegisterResponse
import com.bangkitacademy.foodergyapps.utils.RETROFIT_TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.*

class FoodRepository @Inject constructor  (
    private val apiService: ApiService,
    private val userPreference: UserPreference
    ) {
//    private val _userLogin = MutableLiveData<LoginResult>()
//    val userLogin: LiveData<LoginResult> = _userLogin

    private val _userLogin = MutableLiveData<LoginResult>()
    val userLogin: LiveData<LoginResult> = _userLogin

//    private val _userLogin = MutableLiveData<LoginItem>()
//    val userLogin: LiveData<LoginItem> = _userLogin

    private val _userRegister = MutableLiveData<List<RegisterItem>>()
    val userRegister: LiveData<List<RegisterItem>> = _userRegister

    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun loginUser(email: String, password: String){
        _isLoading.value = true
        val client2 = apiService.loginUser(email, password)
        client2.enqueue(object : Callback<LoginResponse>{
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>) {
                    _isLoading.value = true
                    if(response.isSuccessful){
                        _toastMessage.value = response.body()?.message
                        _userLogin.value = response.body()?.loginResult
                        Log.d(RETROFIT_TAG, response.body()?.message.toString())
                        Log.d(RETROFIT_TAG, response.body()?.loginResult?.email ?: "email")
                    }
                if (!response.isSuccessful) {
                    Log.e(RETROFIT_TAG, "on Failure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _toastMessage.value = t.message
                _isLoading.value = false
                Log.e(RETROFIT_TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun registerUser(name: String,email: String, password: String, alergi :String){
        _isLoading.value = true
        val client = apiService.createUser(name, email, password, alergi)
        client.enqueue(object : Callback<RegisterResponse>{
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null){
                        _isLoading.value = false
                        _userRegister.postValue(response.body()?.register)
                    }else{
                        _isLoading.value = false
                        Log.e(RETROFIT_TAG, "on Failure: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e(RETROFIT_TAG, "onFailure: ${t.message}")
            }

        })
    }
}
