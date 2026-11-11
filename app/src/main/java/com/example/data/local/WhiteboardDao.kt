package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.FileType
import com.example.data.model.WhiteboardFile
import kotlinx.coroutines.flow.Flow

@Dao
interface WhiteboardDao {
    @Query("SELECT * FROM whiteboard_files ORDER BY lastModified DESC")
    fun getAllFiles(): Flow<List<WhiteboardFile>>

    @Query("SELECT * FROM whiteboard_files ORDER BY lastModified DESC LIMIT :limit")
    fun getRecentFiles(limit: Int = 10): Flow<List<WhiteboardFile>>

    @Query("SELECT * FROM whiteboard_files WHERE type = :type ORDER BY lastModified DESC")
    fun getFilesByType(type: FileType): Flow<List<WhiteboardFile>>

    @Query("SELECT * FROM whiteboard_files WHERE isFavorite = 1 ORDER BY lastModified DESC")
    fun getFavoriteFiles(): Flow<List<WhiteboardFile>>

    @Query("SELECT * FROM whiteboard_files WHERE title LIKE '%' || :query || '%' OR category LIKE '%' || :query || '%' ORDER BY lastModified DESC")
    fun searchFiles(query: String): Flow<List<WhiteboardFile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFile(file: WhiteboardFile): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(files: List<WhiteboardFile>)

    @Update
    suspend fun updateFile(file: WhiteboardFile)

    @Delete
    suspend fun deleteFile(file: WhiteboardFile)

    @Query("DELETE FROM whiteboard_files WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("SELECT COUNT(*) FROM whiteboard_files")
    suspend fun getCount(): Int
}
