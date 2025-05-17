package com.example.loomi.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize
import java.util.UUID

@IgnoreExtraProperties
@Parcelize
data class Section(
    var docId: String = "",
    val order: Int = 0,
    val title: String = "",
    val content : List<Content> = emptyList(),
) : Parcelable
