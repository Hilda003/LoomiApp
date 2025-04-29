package com.example.loomi.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize
import java.util.UUID

@IgnoreExtraProperties
@Parcelize
data class Content(
    val title: String = "",
    val description: String = "",
    val descriptionList: List<String> = emptyList(),
    val url: String = "",
    val type: ContentType = ContentType.EXPLANATION,
    val choices: List<String>? = null,
    val code: String? = null,
    val correctAnswer: List<String>? = null,
    val drag: String? = null,
    val text: String? = null,
    val question: String? = null
) : Parcelable

enum class ContentType {
    EXPLANATION,
//    FILL_IN_BLANK,
    DRAG_AND_DROP,
    MULTIPLE_CHOICE;

    companion object {
        fun fromString(value: String): ContentType {
            return try {
                valueOf(value.uppercase())
            } catch (e: IllegalArgumentException) {
                EXPLANATION
            }
        }
    }
}
