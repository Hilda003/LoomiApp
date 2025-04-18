package com.example.loomi.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Section(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val content: List<Content>,
    val isComplete: Boolean = false
): Parcelable {
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        TODO("Not yet implemented")
    }
}

