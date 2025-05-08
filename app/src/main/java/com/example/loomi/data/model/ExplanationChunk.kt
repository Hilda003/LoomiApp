package com.example.loomi.data.model

data class ExplanationChunk(val type: ChunkType, val content: String)

enum class ChunkType { TEXT, CODE, OUTPUT }

fun parseChunks(rawList: List<String>): List<ExplanationChunk> {
    return rawList.map {
        when {
            it.startsWith("[code]") -> ExplanationChunk(ChunkType.CODE, it.removePrefix("[code]"))
            it.startsWith("[output]") -> ExplanationChunk(ChunkType.OUTPUT, it.removePrefix("[output]"))
            it.startsWith("[text]") -> ExplanationChunk(ChunkType.TEXT, it.removePrefix("[text]"))
            else -> ExplanationChunk(ChunkType.TEXT, it) // fallback
        }
    }
}
