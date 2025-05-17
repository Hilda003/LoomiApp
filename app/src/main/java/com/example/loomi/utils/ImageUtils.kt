package com.example.loomi.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

object ImageUtil {
    fun generateInitialBitmap(initial: String): Bitmap {
        val size = 200
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paintCircle = Paint().apply {
            color = Color.LTGRAY
            isAntiAlias = true
        }

        val paintText = Paint().apply {
            color = Color.WHITE
            textSize = 80f
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paintCircle)
        val yPos = (canvas.height / 2 - (paintText.descent() + paintText.ascent()) / 2)
        canvas.drawText(initial, size / 2f, yPos, paintText)

        return bitmap
    }
}
