package com.example.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.FileType
import com.example.data.model.WhiteboardFile
import com.example.data.repository.WhiteboardRepository
import com.example.ui.common.AppLanguage
import com.example.ui.common.AppThemeMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    HOME,
    WHITEBOARD,
    MY_FILES,
    TOOLS,
    SETTINGS,
    // Sub-screens
    MY_WHITEBOARDS,
    PDF_TOOLS,
    MY_NOTES,
    MATHS_TOOLS,
    GEOMETRY_TOOLS,
    OCR_SCANNER,
    MIND_MAP,
    FLOWCHART,
    QUIZ_MAKER,
    PRESENTATION,
    SCREEN_RECORDER,
    VOICE_RECORDER,
    LESSON_PLANNER,
    TEMPLATES_GALLERY
}

enum class CanvasTool {
    PEN,
    PENCIL,
    HIGHLIGHTER,
    ERASER,
    LINE,
    RECTANGLE,
    CIRCLE,
    ARROW,
    TEXT
}

enum class CanvasBackground {
    PLAIN,
    RULED,
    GRID,
    GRAPH,
    CHALKBOARD,
    DARK
}

data class DrawPoint(val x: Float, val y: Float)

data class DrawStroke(
    val points: List<DrawPoint>,
    val color: Color,
    val strokeWidth: Float,
    val alpha: Float = 1.0f,
    val tool: CanvasTool = CanvasTool.PEN
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WhiteboardRepository

    init {
        val db = AppDatabase.getDatabase(application, viewModelScope)
        repository = WhiteboardRepository(db.whiteboardDao())
    }

    // Navigation State
    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _screenStack = MutableStateFlow<List<AppScreen>>(listOf(AppScreen.HOME))
    val screenStack: StateFlow<List<AppScreen>> = _screenStack.asStateFlow()

    // App Preferences State
    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _themeMode = MutableStateFlow(AppThemeMode.SYSTEM)
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    // Search & Filter State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Room Database Observables
    val recentFiles: StateFlow<List<WhiteboardFile>> = repository.recentFiles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allFiles: StateFlow<List<WhiteboardFile>> = repository.allFiles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteFiles: StateFlow<List<WhiteboardFile>> = repository.favoriteFiles
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredFiles: StateFlow<List<WhiteboardFile>> = combine(
        repository.allFiles,
        _searchQuery,
        _selectedCategory
    ) { files, query, category ->
        files.filter { file ->
            val matchesQuery = query.isBlank() || file.title.contains(query, ignoreCase = true) ||
                    file.category.contains(query, ignoreCase = true) ||
                    file.previewSnippet.contains(query, ignoreCase = true)
            val matchesCategory = when (category) {
                "All" -> true
                "Whiteboards" -> file.type == FileType.WHITEBOARD
                "Notes" -> file.type == FileType.NOTE
                "PDF Tools" -> file.type == FileType.PDF
                "Maths & STEM" -> file.type in listOf(FileType.WHITEBOARD, FileType.QUIZ)
                "Teaching" -> file.type in listOf(FileType.LESSON, FileType.MIND_MAP, FileType.FLOWCHART)
                else -> file.category.equals(category, ignoreCase = true)
            }
            matchesQuery && matchesCategory
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Whiteboard Canvas State
    val strokes = MutableStateFlow<List<DrawStroke>>(emptyList())
    private val redoStrokes = MutableStateFlow<List<DrawStroke>>(emptyList())

    val selectedTool = MutableStateFlow(CanvasTool.PEN)
    val selectedColor = MutableStateFlow(Color(0xFF1746A2))
    val strokeWidth = MutableStateFlow(6f)
    val canvasBackground = MutableStateFlow(CanvasBackground.PLAIN)
    val currentPage = MutableStateFlow(1)
    val totalPages = MutableStateFlow(1)
    val currentBoardTitle = MutableStateFlow("Physics Class 10 - Ray Optics")

    // Navigation Methods
    fun navigateTo(screen: AppScreen) {
        if (_currentScreen.value != screen) {
            _screenStack.value = _screenStack.value + screen
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        val stack = _screenStack.value
        return if (stack.size > 1) {
            val newStack = stack.dropLast(1)
            _screenStack.value = newStack
            _currentScreen.value = newStack.last()
            true
        } else if (_currentScreen.value != AppScreen.HOME) {
            _currentScreen.value = AppScreen.HOME
            _screenStack.value = listOf(AppScreen.HOME)
            true
        } else {
            false
        }
    }

    fun switchBottomTab(screen: AppScreen) {
        _currentScreen.value = screen
        _screenStack.value = listOf(screen)
    }

    // Search and Language
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun toggleLanguage() {
        _currentLanguage.value = if (_currentLanguage.value == AppLanguage.ENGLISH) {
            AppLanguage.HINDI
        } else {
            AppLanguage.ENGLISH
        }
    }

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
    }

    fun setThemeMode(mode: AppThemeMode) {
        _themeMode.value = mode
    }

    // Canvas Actions
    fun addStroke(stroke: DrawStroke) {
        strokes.value = strokes.value + stroke
        redoStrokes.value = emptyList()
    }

    fun undoStroke() {
        val current = strokes.value
        if (current.isNotEmpty()) {
            val last = current.last()
            strokes.value = current.dropLast(1)
            redoStrokes.value = redoStrokes.value + last
        }
    }

    fun redoStroke() {
        val redoList = redoStrokes.value
        if (redoList.isNotEmpty()) {
            val last = redoList.last()
            redoStrokes.value = redoList.dropLast(1)
            strokes.value = strokes.value + last
        }
    }

    fun clearCanvas() {
        strokes.value = emptyList()
        redoStrokes.value = emptyList()
    }

    fun setCanvasTool(tool: CanvasTool) {
        selectedTool.value = tool
    }

    fun setCanvasColor(color: Color) {
        selectedColor.value = color
    }

    fun setStrokeWidth(width: Float) {
        strokeWidth.value = width
    }

    fun setCanvasBackground(bg: CanvasBackground) {
        canvasBackground.value = bg
    }

    fun addNewPage() {
        totalPages.value = totalPages.value + 1
        currentPage.value = totalPages.value
        clearCanvas()
    }

    fun nextPage() {
        if (currentPage.value < totalPages.value) {
            currentPage.value = currentPage.value + 1
        }
    }

    fun prevPage() {
        if (currentPage.value > 1) {
            currentPage.value = currentPage.value - 1
        }
    }

    // File Management
    fun createNewWhiteboard(title: String = "Untitled Board") {
        viewModelScope.launch {
            val id = repository.createNewWhiteboard(title)
            currentBoardTitle.value = title
            clearCanvas()
            navigateTo(AppScreen.WHITEBOARD)
        }
    }

    fun toggleFavorite(file: WhiteboardFile) {
        viewModelScope.launch {
            repository.toggleFavorite(file)
        }
    }

    fun deleteFile(file: WhiteboardFile) {
        viewModelScope.launch {
            repository.deleteFile(file)
        }
    }
}
