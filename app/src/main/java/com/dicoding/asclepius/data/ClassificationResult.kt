package com.dicoding.asclepius.data

import android.os.Parcel
import android.os.Parcelable

data class ClassificationResult(val label: String, val confidence: Float) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readFloat()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(label)
        parcel.writeFloat(confidence)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ClassificationResult> {
        override fun createFromParcel(parcel: Parcel): ClassificationResult {
            return ClassificationResult(parcel)
        }

        override fun newArray(size: Int): Array<ClassificationResult?> {
            return arrayOfNulls(size)
        }
    }
}
