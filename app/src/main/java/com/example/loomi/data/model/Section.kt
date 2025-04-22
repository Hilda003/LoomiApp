package com.example.loomi.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize
import java.util.UUID

@IgnoreExtraProperties
@Parcelize
data class Section(
    val id: Int = 0,
    val title: String = "",
    val isLocked: Boolean = true,
    val content : List<Content> = emptyList(),
) : Parcelable
