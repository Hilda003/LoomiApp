package com.example.loomi.utils


import com.example.loomi.data.model.Content
import com.example.loomi.data.model.ContentType

object ContentParser {
    fun parseContent(raw: Map<String, Any>): Content? {
        val typeStr = raw["type"] as? String ?: return null
        val type = ContentType.fromString(typeStr)
        val title = raw["title"] as? String ?: ""

        return when (type) {
            ContentType.EXPLANATION -> {
                val dataRaw = raw["data"]
                val descriptionList = when (dataRaw) {
                    is List<*> -> dataRaw.map { it.toString() }
                    is Map<*, *> -> listOf(dataRaw["text"] as? String ?: "")
                    else -> emptyList()
                }
                Content(
                    type = type,
                    title = title,
                    descriptionList = descriptionList
                )
            }

            ContentType.MULTIPLE_CHOICE -> {
                val dataMap = raw["data"] as? Map<*, *> ?: return null
                Content(
                    type = type,
                    title = title,
                    question = dataMap["question"] as? String,
                    code = dataMap["code"] as? String,
                    choices = dataMap["choices"] as? List<String>,
                    correctAnswer = (dataMap["correctAnswer"] as? List<*>)?.mapNotNull { it?.toString() }
                )
            }

            ContentType.DRAG_AND_DROP -> {
                val dataMap = raw["data"] as? Map<*, *> ?: return null
                Content(
                    type = type,
                    title = title,
                    description = dataMap["text"] as? String ?: "",
                    choices = dataMap["drag"] as? List<String>,
                    correctAnswer = (dataMap["correctAnswer"] as? List<*>)?.mapNotNull { it?.toString() }
                )
            }
        }
    }
}
