package com.example.loomi.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Material(
    val id: String = "",
    val title: String = "",
    val imgResId: String? = "",
    val sections: List<Section> = emptyList()
) : Parcelable
