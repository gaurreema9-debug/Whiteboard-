package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.FileType
import com.example.data.model.WhiteboardFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Converters {
    @TypeConverter
    fun fromFileType(value: FileType): String = value.name

    @TypeConverter
    fun toFileType(value: String): FileType = try {
        FileType.valueOf(value)
    } catch (e: Exception) {
        FileType.WHITEBOARD
    }
}

@Database(entities = [WhiteboardFile::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun whiteboardDao(): WhiteboardDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "whiteboard_india_db"
                )
                .addCallback(DatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.whiteboardDao())
                    }
                }
            }

            suspend fun populateInitialData(dao: WhiteboardDao) {
                val now = System.currentTimeMillis()
                val initialFiles = listOf(
                    WhiteboardFile(
                        title = "NCERT Class 10 Physics: Ray Optics",
                        type = FileType.WHITEBOARD,
                        pageCount = 3,
                        lastModified = now - 1000 * 60 * 30,
                        isFavorite = true,
                        category = "Science",
                        thumbnailColorHex = "#1746A2",
                        previewSnippet = "Snell's Law, Convex & Concave lenses diagram notes"
                    ),
                    WhiteboardFile(
                        title = "CBSE Class 12 Calculus & Integrals",
                        type = FileType.WHITEBOARD,
                        pageCount = 5,
                        lastModified = now - 1000 * 60 * 120,
                        isFavorite = true,
                        category = "Mathematics",
                        thumbnailColorHex = "#14B8A6",
                        previewSnippet = "Integration by parts formula sheet and trigonometric proofs"
                    ),
                    WhiteboardFile(
                        title = "Hindi Vyakaran: Samas & Sandhi",
                        type = FileType.NOTE,
                        pageCount = 2,
                        lastModified = now - 1000 * 60 * 60 * 5,
                        isFavorite = false,
                        category = "Languages",
                        thumbnailColorHex = "#FF9933",
                        previewSnippet = "संधि और समास के प्रमुख भेद एवं उदाहरण"
                    ),
                    WhiteboardFile(
                        title = "Indian Freedom Movement (1857-1947)",
                        type = FileType.MIND_MAP,
                        pageCount = 1,
                        lastModified = now - 1000 * 60 * 60 * 24,
                        isFavorite = true,
                        category = "Social Studies",
                        thumbnailColorHex = "#138808",
                        previewSnippet = "Chronological mind map of freedom struggle events"
                    ),
                    WhiteboardFile(
                        title = "Quadratic Equations Practice Test",
                        type = FileType.QUIZ,
                        pageCount = 10,
                        lastModified = now - 1000 * 60 * 60 * 48,
                        isFavorite = false,
                        category = "Exams",
                        thumbnailColorHex = "#D97706",
                        previewSnippet = "10 standard MCQs with instant step-by-step scoring"
                    )
                )
                dao.insertAll(initialFiles)
            }
        }
    }
}
