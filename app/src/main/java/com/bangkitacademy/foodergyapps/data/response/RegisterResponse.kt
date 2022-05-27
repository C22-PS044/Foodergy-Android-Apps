package com.bangkitacademy.foodergyapps.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class RegisterResponse (
    @field:SerializedName("register")
    val register: List<RegisterItem>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
):Parcelable

@Parcelize
data class RegisterItem(

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("alergi")
    val alergi: String


): Parcelable