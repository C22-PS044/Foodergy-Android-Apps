package com.bangkitacademy.foodergyapps.data.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DataHistory (
    var namaMakanan : String?,
    var komposisi : String?,
    var rekomendasi: String?
) :Parcelable