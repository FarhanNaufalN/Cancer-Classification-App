package com.dicoding.asclepius.data

import android.os.Parcel
import android.os.Parcelable

data class ParcelableClassifications(val confidence: Float, val label: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readFloat(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(confidence)
        parcel.writeString(label)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableClassifications> {
        override fun createFromParcel(parcel: Parcel): ParcelableClassifications {
            return ParcelableClassifications(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableClassifications?> {
            return arrayOfNulls(size)
        }
    }
}
