package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.common.AppLanguage
import com.example.ui.common.AppThemeMode
import com.example.ui.common.LanguageStrings
import com.example.ui.components.BottomNavBar
import com.example.ui.components.TopAppBarHeader
import com.example.ui.screens.AudioVideoRecorderScreen
import com.example.ui.screens.GenericToolScreen
import com.example.ui.screens.GeometryToolsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MathsToolsScreen
import com.example.ui.screens.MyFilesScreen
import com.example.ui.screens.OCRScannerScreen
import com.example.ui.screens.QuizMakerScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.ToolsScreen
import com.example.ui.screens.WhiteboardScreen
import com.example.ui.theme.WhiteboardIndiaTheme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()
            val isDark = when (themeMode) {
                AppThemeMode.LIGHT -> false
                AppThemeMode.DARK -> true
                AppThemeMode.SYSTEM -> isSystemInDarkTheme()
            }

            WhiteboardIndiaTheme(darkTheme = isDark) {
                var showSplash by remember { mutableStateOf(true) }
                val language by viewModel.currentLanguage.collectAsStateWithLifecycle()

                if (showSplash) {
                    SplashScreen(
                        language = language,
                        onContinue = { showSplash = false }
                    )
                } else {
                    WhiteboardAppRoot(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun WhiteboardAppRoot(viewModel: MainViewModel) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val language by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val themeMode by viewModel.themeMode.collectAsStateWithLifecycle()

    val isSubScreen = currentScreen !in listOf(
        AppScreen.HOME,
        AppScreen.WHITEBOARD,
        AppScreen.MY_FILES,
        AppScreen.TOOLS,
        AppScreen.SETTINGS
    )

    // Handle global back press
    BackHandler(enabled = currentScreen != AppScreen.HOME) {
        viewModel.navigateBack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (currentScreen != AppScreen.WHITEBOARD && !isSubScreen) {
                TopAppBarHeader(
                    title = when (currentScreen) {
                        AppScreen.HOME -> LanguageStrings.get("app_full_name", language)
                        AppScreen.MY_FILES -> LanguageStrings.get("nav_files", language)
                        AppScreen.TOOLS -> LanguageStrings.get("nav_tools", language)
                        AppScreen.SETTINGS -> LanguageStrings.get("nav_settings", language)
                        else -> LanguageStrings.get("app_name", language)
                    },
                    showBackButton = false,
                    language = language,
                    onToggleLanguage = { viewModel.toggleLanguage() },
                    themeMode = themeMode,
                    onToggleTheme = {
                        viewModel.setThemeMode(
                            if (themeMode == AppThemeMode.DARK) AppThemeMode.LIGHT else AppThemeMode.DARK
                        )
                    }
                )
            }
        },
        bottomBar = {
            if (currentScreen in listOf(
                    AppScreen.HOME,
                    AppScreen.WHITEBOARD,
                    AppScreen.MY_FILES,
                    AppScreen.TOOLS,
                    AppScreen.SETTINGS
                ) && currentScreen != AppScreen.WHITEBOARD
            ) {
                BottomNavBar(
                    currentScreen = currentScreen,
                    language = language,
                    onTabSelected = { tab -> viewModel.switchBottomTab(tab) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    if (currentScreen == AppScreen.WHITEBOARD || isSubScreen)
                        androidx.compose.foundation.layout.PaddingValues()
                    else
                        innerPadding
                )
        ) {
            when (currentScreen) {
                AppScreen.HOME -> HomeScreen(viewModel = viewModel)
                AppScreen.WHITEBOARD -> WhiteboardScreen(viewModel = viewModel)
                AppScreen.MY_FILES -> MyFilesScreen(viewModel = viewModel)
                AppScreen.TOOLS -> ToolsScreen(viewModel = viewModel)
                AppScreen.SETTINGS -> SettingsScreen(viewModel = viewModel)

                // Sub-screens
                AppScreen.MY_WHITEBOARDS -> MyFilesScreen(viewModel = viewModel)
                AppScreen.PDF_TOOLS -> GenericToolScreen("tool_pdf", "tool_pdf_desc", viewModel)
                AppScreen.MY_NOTES -> MyFilesScreen(viewModel = viewModel)
                AppScreen.MATHS_TOOLS -> MathsToolsScreen(viewModel = viewModel)
                AppScreen.GEOMETRY_TOOLS -> GeometryToolsScreen(viewModel = viewModel)
                AppScreen.OCR_SCANNER -> OCRScannerScreen(viewModel = viewModel)
                AppScreen.MIND_MAP -> GenericToolScreen("tool_mindmap", "tool_mindmap_desc", viewModel)
                AppScreen.FLOWCHART -> GenericToolScreen("tool_flowchart", "tool_flowchart_desc", viewModel)
                AppScreen.QUIZ_MAKER -> QuizMakerScreen(viewModel = viewModel)
                AppScreen.PRESENTATION -> GenericToolScreen("tool_presentation", "tool_presentation_desc", viewModel)
                AppScreen.SCREEN_RECORDER -> AudioVideoRecorderScreen(isScreenRec = true, viewModel = viewModel)
                AppScreen.VOICE_RECORDER -> AudioVideoRecorderScreen(isScreenRec = false, viewModel = viewModel)
                AppScreen.LESSON_PLANNER -> GenericToolScreen("tool_planner", "tool_planner_desc", viewModel)
                AppScreen.TEMPLATES_GALLERY -> GenericToolScreen("tool_templates", "tool_templates_desc", viewModel)
            }
        }
    }
}
