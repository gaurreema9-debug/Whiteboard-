package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.automirrored.outlined.AltRoute
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.PresentToAll
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.FileType
import com.example.data.model.WhiteboardFile
import com.example.ui.common.AppLanguage
import com.example.ui.common.LanguageStrings
import com.example.ui.components.AdMobBannerPlaceholder
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SaffronOrange
import com.example.ui.theme.SecondaryTeal
import com.example.viewmodel.AppScreen
import com.example.viewmodel.MainViewModel

data class HomeToolCardItem(
    val id: String,
    val titleKey: String,
    val descKey: String,
    val icon: ImageVector,
    val screen: AppScreen,
    val badge: String? = null,
    val accentColor: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val recentFiles by viewModel.recentFiles.collectAsStateWithLifecycle()
    val filteredFiles by viewModel.filteredFiles.collectAsStateWithLifecycle()

    val categories = listOf("All", "Whiteboards", "Notes", "PDF Tools", "Maths & STEM", "Teaching")

    val allTools = listOf(
        HomeToolCardItem("new_wb", "tool_new_wb", "tool_new_wb_desc", Icons.Default.Add, AppScreen.WHITEBOARD, "HOT", PrimaryBlue),
        HomeToolCardItem("my_wb", "tool_my_wb", "tool_my_wb_desc", Icons.Default.Draw, AppScreen.MY_WHITEBOARDS, null, Color(0xFF2563EB)),
        HomeToolCardItem("pdf_tools", "tool_pdf", "tool_pdf_desc", Icons.Default.PictureAsPdf, AppScreen.PDF_TOOLS, "PDF", Color(0xFFDC2626)),
        HomeToolCardItem("notes", "tool_notes", "tool_notes_desc", Icons.Default.Description, AppScreen.MY_NOTES, null, Color(0xFF059669)),
        HomeToolCardItem("maths", "tool_maths", "tool_maths_desc", Icons.Default.Calculate, AppScreen.MATHS_TOOLS, "STEM", Color(0xFF7C3AED)),
        HomeToolCardItem("geometry", "tool_geometry", "tool_geometry_desc", Icons.Default.SquareFoot, AppScreen.GEOMETRY_TOOLS, "MATH", Color(0xFF0284C7)),
        HomeToolCardItem("ocr", "tool_ocr", "tool_ocr_desc", Icons.Default.Scanner, AppScreen.OCR_SCANNER, "AI/ML", Color(0xFFD97706)),
        HomeToolCardItem("mindmap", "tool_mindmap", "tool_mindmap_desc", Icons.Outlined.AccountTree, AppScreen.MIND_MAP, null, SecondaryTeal),
        HomeToolCardItem("flowchart", "tool_flowchart", "tool_flowchart_desc", Icons.AutoMirrored.Outlined.AltRoute, AppScreen.FLOWCHART, null, Color(0xFF4F46E5)),
        HomeToolCardItem("quiz", "tool_quiz", "tool_quiz_desc", Icons.Default.Quiz, AppScreen.QUIZ_MAKER, "TEST", Color(0xFFEA580C)),
        HomeToolCardItem("presentation", "tool_presentation", "tool_presentation_desc", Icons.Default.PresentToAll, AppScreen.PRESENTATION, "CLASS", PrimaryBlue),
        HomeToolCardItem("screen_rec", "tool_screen_rec", "tool_screen_rec_desc", Icons.Default.Videocam, AppScreen.SCREEN_RECORDER, "REC", Color(0xFFE11D48)),
        HomeToolCardItem("voice_rec", "tool_voice_rec", "tool_voice_rec_desc", Icons.Default.Mic, AppScreen.VOICE_RECORDER, "AUDIO", Color(0xFF9333EA)),
        HomeToolCardItem("planner", "tool_planner", "tool_planner_desc", Icons.AutoMirrored.Filled.EventNote, AppScreen.LESSON_PLANNER, "PLAN", Color(0xFF0D9488))
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("home_screen_content"),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // Search Bar Section
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.updateSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_search_input"),
                    placeholder = {
                        Text(
                            text = LanguageStrings.get("search_hint", language),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        }

        // Hero Banner: New Whiteboard Launchpad
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .testTag("hero_new_whiteboard_card"),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(PrimaryBlue, Color(0xFF1E3A8A), SecondaryTeal)
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SaffronOrange)
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "FREE FOREVER",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Text(
                                text = LanguageStrings.get("tagline", language),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = LanguageStrings.get("new_whiteboard", language),
                            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Text(
                            text = LanguageStrings.get("new_whiteboard_sub", language),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.createNewWhiteboard() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = PrimaryBlue
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("quick_start_canvas_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                                Text(
                                    text = if (language == AppLanguage.HINDI) "कैनवास खोलें" else "Open Blank Canvas",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                Text(
                    text = LanguageStrings.get("categories", language),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        val isSelected = selectedCategory == category
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectCategory(category) },
                            label = { Text(category) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.testTag("category_chip_$category")
                        )
                    }
                }
            }
        }

        // Recently Opened Files Carousel
        item {
            Column(modifier = Modifier.padding(top = 14.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = LanguageStrings.get("recent_files", language),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = LanguageStrings.get("see_all", language),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .clickable { viewModel.navigateTo(AppScreen.MY_FILES) }
                            .testTag("see_all_recent_files")
                    )
                }

                if (recentFiles.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "कोई हालिया फ़ाइल नहीं है" else "No recent files yet. Start your first board!",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(recentFiles) { file ->
                            RecentFileCard(
                                file = file,
                                onOpen = {
                                    viewModel.currentBoardTitle.value = file.title
                                    viewModel.navigateTo(AppScreen.WHITEBOARD)
                                },
                                onToggleFavorite = { viewModel.toggleFavorite(file) }
                            )
                        }
                    }
                }
            }
        }

        // Core 14 Educational Tools Grid
        item {
            Column(modifier = Modifier.padding(top = 18.dp, start = 16.dp, end = 16.dp)) {
                Text(
                    text = if (language == AppLanguage.HINDI) "प्रमुख शैक्षणिक उपकरण (१४)" else "Core Educational Tools (14)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        // Render tool cards in pairs
        items(allTools.chunked(2)) { pair ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                pair.forEach { tool ->
                    Box(modifier = Modifier.weight(1f)) {
                        HomeToolCard(
                            tool = tool,
                            language = language,
                            onClick = { viewModel.navigateTo(tool.screen) }
                        )
                    }
                }
                if (pair.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        // Bottom AdMob Test Banner
        item {
            Spacer(modifier = Modifier.height(14.dp))
            AdMobBannerPlaceholder(language = language)
        }
    }
}

@Composable
fun RecentFileCard(
    file: WhiteboardFile,
    onOpen: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(200.dp)
            .clickable { onOpen() }
            .testTag("recent_file_${file.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Header Color Strip with Type Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(android.graphics.Color.parseColor(file.thumbnailColorHex)).copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when (file.type) {
                            FileType.WHITEBOARD -> Icons.Default.Draw
                            FileType.PDF -> Icons.Default.PictureAsPdf
                            FileType.NOTE -> Icons.Default.Description
                            FileType.QUIZ -> Icons.Default.Quiz
                            FileType.MIND_MAP -> Icons.Outlined.AccountTree
                            FileType.FLOWCHART -> Icons.AutoMirrored.Outlined.AltRoute
                            FileType.LESSON -> Icons.AutoMirrored.Filled.EventNote
                        },
                        contentDescription = file.type.name,
                        tint = Color(android.graphics.Color.parseColor(file.thumbnailColorHex)),
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = onToggleFavorite,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (file.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (file.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = file.title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = file.previewSnippet,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${file.pageCount} ${if (file.pageCount > 1) "pages" else "page"}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = file.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun HomeToolCard(
    tool: HomeToolCardItem,
    language: AppLanguage,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(118.dp)
            .clickable { onClick() }
            .testTag("tool_card_${tool.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(tool.accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = tool.icon,
                        contentDescription = tool.titleKey,
                        tint = tool.accentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                tool.badge?.let { badge ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(tool.accentColor.copy(alpha = 0.12f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = badge,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = tool.accentColor
                        )
                    }
                }
            }

            Column {
                Text(
                    text = LanguageStrings.get(tool.titleKey, language),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = LanguageStrings.get(tool.descKey, language),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
