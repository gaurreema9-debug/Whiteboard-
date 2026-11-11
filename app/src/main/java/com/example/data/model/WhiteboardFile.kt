package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class FileType {
    WHITEBOARD,
    PDF,
    NOTE,
    MIND_MAP,
    FLOWCHART,
    QUIZ,
    LESSON
}

@Entity(tableName = "whiteboard_files")
data class WhiteboardFile(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val type: FileType,
    val pageCount: Int = 1,
    val lastModified: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val category: String = "General",
    val thumbnailColorHex: String = "#1746A2",
    val previewSnippet: String = ""
)
