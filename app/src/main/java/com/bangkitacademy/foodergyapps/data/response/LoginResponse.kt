package com.bangkitacademy.foodergyapps.data.response

data class LoginResponse(
    val error: Boolean,
    val loginResult: LoginResult,
    val message: String
)