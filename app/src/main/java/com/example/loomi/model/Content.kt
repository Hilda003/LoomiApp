package com.example.loomi.model

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Parcelize
data class Content(
    val id: String = UUID.randomUUID().toString(),
    val type: ContentType,
    val title: String,
    val options : List<String> = emptyList(),
    val description: String,
    val answer: String? = null,

//    var isComplete: Boolean = false
) : Parcelable {
    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        TODO("Not yet implemented")
    }
}

@Parcelize
enum class ContentType {
    EXPLANATION,
    CODE_SNIPPET,
    FILL_IN_BLANK,
    MULTIPLE_CHOICE,
    RESULT
}

