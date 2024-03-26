package com.dicoding.asclepius.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Classifications(
    val category: String,
    val score: Float
) : Parcelable
