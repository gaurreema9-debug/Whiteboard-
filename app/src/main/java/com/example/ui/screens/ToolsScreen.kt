package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.PresentToAll
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Scanner
import androidx.compose.material.icons.filled.SquareFoot
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.AltRoute
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.common.AppLanguage
import com.example.ui.common.LanguageStrings
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SecondaryTeal
import com.example.viewmodel.AppScreen
import com.example.viewmodel.MainViewModel

data class ToolSection(
    val titleEn: String,
    val titleHi: String,
    val tools: List<SectionToolItem>
)

data class SectionToolItem(
    val titleKey: String,
    val descKey: String,
    val icon: ImageVector,
    val screen: AppScreen,
    val color: Color
)

@Composable
fun ToolsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsStateWithLifecycle()

    val sections = listOf(
        ToolSection(
            titleEn = "Visual Drawing & Diagrams",
            titleHi = "चित्रण व आरेख उपकरण",
            tools = listOf(
                SectionToolItem("tool_new_wb", "tool_new_wb_desc", Icons.Default.Draw, AppScreen.WHITEBOARD, PrimaryBlue),
                SectionToolItem("tool_mindmap", "tool_mindmap_desc", Icons.Outlined.AccountTree, AppScreen.MIND_MAP, SecondaryTeal),
                SectionToolItem("tool_flowchart", "tool_flowchart_desc", Icons.Outlined.AltRoute, AppScreen.FLOWCHART, Color(0xFF4F46E5)),
                SectionToolItem("tool_templates", "tool_templates_desc", Icons.Default.Style, AppScreen.TEMPLATES_GALLERY, Color(0xFFD97706))
            )
        ),
        ToolSection(
            titleEn = "STEM & Mathematics Studio",
            titleHi = "गणित एवं विज्ञान स्टूडियो",
            tools = listOf(
                SectionToolItem("tool_maths", "tool_maths_desc", Icons.Default.Calculate, AppScreen.MATHS_TOOLS, Color(0xFF7C3AED)),
                SectionToolItem("tool_geometry", "tool_geometry_desc", Icons.Default.SquareFoot, AppScreen.GEOMETRY_TOOLS, Color(0xFF0284C7)),
                SectionToolItem("tool_quiz", "tool_quiz_desc", Icons.Default.Quiz, AppScreen.QUIZ_MAKER, Color(0xFFEA580C))
            )
        ),
        ToolSection(
            titleEn = "Teaching & Digital Classroom",
            titleHi = "अध्यापन एवं डिजिटल क्लासरूम",
            tools = listOf(
                SectionToolItem("tool_presentation", "tool_presentation_desc", Icons.Default.PresentToAll, AppScreen.PRESENTATION, PrimaryBlue),
                SectionToolItem("tool_screen_rec", "tool_screen_rec_desc", Icons.Default.Videocam, AppScreen.SCREEN_RECORDER, Color(0xFFE11D48)),
                SectionToolItem("tool_voice_rec", "tool_voice_rec_desc", Icons.Default.Mic, AppScreen.VOICE_RECORDER, Color(0xFF9333EA)),
                SectionToolItem("tool_planner", "tool_planner_desc", Icons.Default.EventNote, AppScreen.LESSON_PLANNER, Color(0xFF0D9488))
            )
        ),
        ToolSection(
            titleEn = "Documents & Smart Scanning",
            titleHi = "दस्तावेज व स्मार्ट स्कैनिंग",
            tools = listOf(
                SectionToolItem("tool_pdf", "tool_pdf_desc", Icons.Default.PictureAsPdf, AppScreen.PDF_TOOLS, Color(0xFFDC2626)),
                SectionToolItem("tool_notes", "tool_notes_desc", Icons.Default.Description, AppScreen.MY_NOTES, Color(0xFF059669)),
                SectionToolItem("tool_ocr", "tool_ocr_desc", Icons.Default.Scanner, AppScreen.OCR_SCANNER, Color(0xFFD97706))
            )
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("tools_catalog_screen"),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        sections.forEach { section ->
            item {
                Text(
                    text = if (language == AppLanguage.HINDI) section.titleHi else section.titleEn,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            items(section.tools) { tool ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.navigateTo(tool.screen) }
                        .testTag("catalog_tool_${tool.screen.name}"),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(tool.color.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = tool.icon,
                                contentDescription = tool.titleKey,
                                tint = tool.color,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 14.dp)
                        ) {
                            Text(
                                text = LanguageStrings.get(tool.titleKey, language),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = LanguageStrings.get(tool.descKey, language),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "Open",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
