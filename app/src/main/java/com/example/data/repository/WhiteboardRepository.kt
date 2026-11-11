package com.example.data.repository

import com.example.data.local.WhiteboardDao
import com.example.data.model.FileType
import com.example.data.model.WhiteboardFile
import kotlinx.coroutines.flow.Flow

class WhiteboardRepository(private val whiteboardDao: WhiteboardDao) {
    val allFiles: Flow<List<WhiteboardFile>> = whiteboardDao.getAllFiles()
    val recentFiles: Flow<List<WhiteboardFile>> = whiteboardDao.getRecentFiles(8)
    val favoriteFiles: Flow<List<WhiteboardFile>> = whiteboardDao.getFavoriteFiles()

    fun getFilesByType(type: FileType): Flow<List<WhiteboardFile>> = whiteboardDao.getFilesByType(type)

    fun searchFiles(query: String): Flow<List<WhiteboardFile>> = whiteboardDao.searchFiles(query)

    suspend fun insertFile(file: WhiteboardFile): Long = whiteboardDao.insertFile(file)

    suspend fun updateFile(file: WhiteboardFile) = whiteboardDao.updateFile(file)

    suspend fun deleteFile(file: WhiteboardFile) = whiteboardDao.deleteFile(file)

    suspend fun deleteById(id: Long) = whiteboardDao.deleteById(id)

    suspend fun createNewWhiteboard(title: String, category: String = "General"): Long {
        val newFile = WhiteboardFile(
            title = title.ifBlank { "Untitled Board ${System.currentTimeMillis() % 1000}" },
            type = FileType.WHITEBOARD,
            pageCount = 1,
            lastModified = System.currentTimeMillis(),
            isFavorite = false,
            category = category,
            thumbnailColorHex = "#1746A2",
            previewSnippet = "Fresh canvas created"
        )
        return whiteboardDao.insertFile(newFile)
    }

    suspend fun toggleFavorite(file: WhiteboardFile) {
        whiteboardDao.updateFile(file.copy(isFavorite = !file.isFavorite))
    }
}
