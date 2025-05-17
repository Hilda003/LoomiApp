package com.example.loomi.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class UserProgress(
    var id: String = "",
    val materialId: String = "",
    val sectionId: String = "",
    val progress: Int = 0,
    val isCompleted: Boolean = false,
    val isLocked: Boolean = true
): Parcelable

