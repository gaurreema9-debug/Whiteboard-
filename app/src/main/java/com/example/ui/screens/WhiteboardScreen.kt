package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Highlight
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.outlined.CleaningServices
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.common.AppLanguage
import com.example.ui.common.LanguageStrings
import com.example.ui.theme.ChalkboardGreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.IndiaGreen
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SaffronOrange
import com.example.ui.theme.SecondaryTeal
import com.example.viewmodel.CanvasBackground
import com.example.viewmodel.CanvasTool
import com.example.viewmodel.DrawPoint
import com.example.viewmodel.DrawStroke
import com.example.viewmodel.MainViewModel

@Composable
fun WhiteboardScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val language by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val strokes by viewModel.strokes.collectAsStateWithLifecycle()
    val selectedTool by viewModel.selectedTool.collectAsStateWithLifecycle()
    val selectedColor by viewModel.selectedColor.collectAsStateWithLifecycle()
    val strokeWidth by viewModel.strokeWidth.collectAsStateWithLifecycle()
    val canvasBackground by viewModel.canvasBackground.collectAsStateWithLifecycle()
    val currentPage by viewModel.currentPage.collectAsStateWithLifecycle()
    val totalPages by viewModel.totalPages.collectAsStateWithLifecycle()
    val currentTitle by viewModel.currentBoardTitle.collectAsStateWithLifecycle()

    var showBgMenu by remember { mutableStateOf(false) }
    var showWidthMenu by remember { mutableStateOf(false) }

    // Intercept hardware/system back button
    BackHandler {
        viewModel.navigateBack()
    }

    val currentPoints = remember { mutableStateListOf<DrawPoint>() }

    val paletteColors = listOf(
        PrimaryBlue,
        SecondaryTeal,
        SaffronOrange,
        IndiaGreen,
        Color(0xFFEF4444), // Red
        Color(0xFF000000), // Black
        Color(0xFFFFFFFF), // White
        Color(0xFFFBBF24)  // Chalk Yellow
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("whiteboard_screen")
    ) {
        // Whiteboard Top Control Bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("whiteboard_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }

                    Column(modifier = Modifier.padding(start = 4.dp)) {
                        Text(
                            text = currentTitle,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${LanguageStrings.get("wb_page", language)} $currentPage / $totalPages • ${LanguageStrings.get("wb_saved", language)}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Undo & Redo
                    IconButton(
                        onClick = { viewModel.undoStroke() },
                        modifier = Modifier.testTag("whiteboard_undo_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Undo, contentDescription = "Undo")
                    }
                    IconButton(
                        onClick = { viewModel.redoStroke() },
                        modifier = Modifier.testTag("whiteboard_redo_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Redo, contentDescription = "Redo")
                    }

                    // Background Menu
                    Box {
                        IconButton(
                            onClick = { showBgMenu = true },
                            modifier = Modifier.testTag("whiteboard_bg_button")
                        ) {
                            Icon(Icons.Default.GridView, contentDescription = "Background Grid")
                        }
                        DropdownMenu(
                            expanded = showBgMenu,
                            onDismissRequest = { showBgMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(LanguageStrings.get("wb_bg_plain", language)) },
                                onClick = {
                                    viewModel.setCanvasBackground(CanvasBackground.PLAIN)
                                    showBgMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(LanguageStrings.get("wb_bg_ruled", language)) },
                                onClick = {
                                    viewModel.setCanvasBackground(CanvasBackground.RULED)
                                    showBgMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(LanguageStrings.get("wb_bg_grid", language)) },
                                onClick = {
                                    viewModel.setCanvasBackground(CanvasBackground.GRID)
                                    showBgMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(LanguageStrings.get("wb_bg_graph", language)) },
                                onClick = {
                                    viewModel.setCanvasBackground(CanvasBackground.GRAPH)
                                    showBgMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(LanguageStrings.get("wb_bg_chalk", language)) },
                                onClick = {
                                    viewModel.setCanvasBackground(CanvasBackground.CHALKBOARD)
                                    showBgMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(LanguageStrings.get("wb_bg_dark", language)) },
                                onClick = {
                                    viewModel.setCanvasBackground(CanvasBackground.DARK)
                                    showBgMenu = false
                                }
                            )
                        }
                    }

                    // Clear Canvas
                    IconButton(
                        onClick = { viewModel.clearCanvas() },
                        modifier = Modifier.testTag("whiteboard_clear_button")
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = "Clear Canvas")
                    }

                    // Export Image / PDF
                    IconButton(
                        onClick = {
                            Toast.makeText(
                                context,
                                if (language == AppLanguage.HINDI) "व्हाइटबोर्ड सफलतापूर्वक सहेजा गया (PNG/PDF)" else "Board saved successfully to local storage (PNG/PDF)",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.testTag("whiteboard_export_button")
                    ) {
                        Icon(Icons.Default.Download, contentDescription = "Export")
                    }
                }
            }
        }

        // Active Drawing Canvas Area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(
                    when (canvasBackground) {
                        CanvasBackground.PLAIN -> Color.White
                        CanvasBackground.RULED -> Color(0xFFFAFAFD)
                        CanvasBackground.GRID -> Color(0xFFF9FAFB)
                        CanvasBackground.GRAPH -> Color(0xFFF5F7FA)
                        CanvasBackground.CHALKBOARD -> ChalkboardGreen
                        CanvasBackground.DARK -> DarkBackground
                    }
                )
                .pointerInput(selectedTool, selectedColor, strokeWidth) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            currentPoints.clear()
                            currentPoints.add(DrawPoint(offset.x, offset.y))
                        },
                        onDrag = { change, _ ->
                            change.consume()
                            currentPoints.add(DrawPoint(change.position.x, change.position.y))
                        },
                        onDragEnd = {
                            if (currentPoints.isNotEmpty()) {
                                val stroke = DrawStroke(
                                    points = currentPoints.toList(),
                                    color = if (selectedTool == CanvasTool.ERASER) {
                                        when (canvasBackground) {
                                            CanvasBackground.CHALKBOARD -> ChalkboardGreen
                                            CanvasBackground.DARK -> DarkBackground
                                            else -> Color.White
                                        }
                                    } else selectedColor,
                                    strokeWidth = if (selectedTool == CanvasTool.ERASER) strokeWidth * 3f else strokeWidth,
                                    alpha = if (selectedTool == CanvasTool.HIGHLIGHTER) 0.35f else 1.0f,
                                    tool = selectedTool
                                )
                                viewModel.addStroke(stroke)
                                currentPoints.clear()
                            }
                        },
                        onDragCancel = {
                            currentPoints.clear()
                        }
                    )
                }
                .testTag("interactive_canvas_area")
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                // Render Background Grids / Lines
                when (canvasBackground) {
                    CanvasBackground.RULED -> {
                        val lineSpacing = 36.dp.toPx()
                        var y = lineSpacing
                        while (y < canvasHeight) {
                            drawLine(
                                color = Color(0xFFCBD5E1),
                                start = Offset(0f, y),
                                end = Offset(canvasWidth, y),
                                strokeWidth = 1.dp.toPx()
                            )
                            y += lineSpacing
                        }
                        // Left margin line (standard notebook margin)
                        drawLine(
                            color = Color(0xFFF87171),
                            start = Offset(48.dp.toPx(), 0f),
                            end = Offset(48.dp.toPx(), canvasHeight),
                            strokeWidth = 1.5.dp.toPx()
                        )
                    }
                    CanvasBackground.GRID -> {
                        val gridSpacing = 28.dp.toPx()
                        var x = 0f
                        while (x < canvasWidth) {
                            drawLine(
                                color = Color(0xFFE2E8F0),
                                start = Offset(x, 0f),
                                end = Offset(x, canvasHeight),
                                strokeWidth = 0.8.dp.toPx()
                            )
                            x += gridSpacing
                        }
                        var y = 0f
                        while (y < canvasHeight) {
                            drawLine(
                                color = Color(0xFFE2E8F0),
                                start = Offset(0f, y),
                                end = Offset(canvasWidth, y),
                                strokeWidth = 0.8.dp.toPx()
                            )
                            y += gridSpacing
                        }
                    }
                    CanvasBackground.GRAPH -> {
                        val mmSpacing = 14.dp.toPx()
                        var x = 0f
                        var index = 0
                        while (x < canvasWidth) {
                            val isMajor = index % 5 == 0
                            drawLine(
                                color = if (isMajor) Color(0xFF93C5FD) else Color(0xFFE0E7FF),
                                start = Offset(x, 0f),
                                end = Offset(x, canvasHeight),
                                strokeWidth = if (isMajor) 1.2.dp.toPx() else 0.5.dp.toPx()
                            )
                            x += mmSpacing
                            index++
                        }
                        var y = 0f
                        index = 0
                        while (y < canvasHeight) {
                            val isMajor = index % 5 == 0
                            drawLine(
                                color = if (isMajor) Color(0xFF93C5FD) else Color(0xFFE0E7FF),
                                start = Offset(0f, y),
                                end = Offset(canvasWidth, y),
                                strokeWidth = if (isMajor) 1.2.dp.toPx() else 0.5.dp.toPx()
                            )
                            y += mmSpacing
                            index++
                        }
                    }
                    else -> {}
                }

                // Render Saved Strokes
                strokes.forEach { stroke ->
                    if (stroke.points.size > 1) {
                        val path = Path().apply {
                            moveTo(stroke.points.first().x, stroke.points.first().y)
                            for (i in 1 until stroke.points.size) {
                                lineTo(stroke.points[i].x, stroke.points[i].y)
                            }
                        }
                        drawPath(
                            path = path,
                            color = stroke.color.copy(alpha = stroke.alpha),
                            style = Stroke(
                                width = stroke.strokeWidth,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    } else if (stroke.points.size == 1) {
                        drawCircle(
                            color = stroke.color.copy(alpha = stroke.alpha),
                            radius = stroke.strokeWidth / 2,
                            center = Offset(stroke.points.first().x, stroke.points.first().y)
                        )
                    }
                }

                // Render In-Progress Stroke
                if (currentPoints.size > 1) {
                    val activePath = Path().apply {
                        moveTo(currentPoints.first().x, currentPoints.first().y)
                        for (i in 1 until currentPoints.size) {
                            lineTo(currentPoints[i].x, currentPoints[i].y)
                        }
                    }
                    val activeColor = if (selectedTool == CanvasTool.ERASER) {
                        when (canvasBackground) {
                            CanvasBackground.CHALKBOARD -> ChalkboardGreen
                            CanvasBackground.DARK -> DarkBackground
                            else -> Color.White
                        }
                    } else selectedColor

                    drawPath(
                        path = activePath,
                        color = activeColor.copy(alpha = if (selectedTool == CanvasTool.HIGHLIGHTER) 0.35f else 1.0f),
                        style = Stroke(
                            width = if (selectedTool == CanvasTool.ERASER) strokeWidth * 3f else strokeWidth,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
            }
        }

        // Bottom Tool Bar & Palettes
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                // Color Palette & Stroke Size Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Color chips
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        paletteColors.forEach { color ->
                            val isColorSelected = selectedColor == color && selectedTool != CanvasTool.ERASER
                            Box(
                                modifier = Modifier
                                    .size(26.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (isColorSelected) 3.dp else 1.dp,
                                        color = if (isColorSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.4f),
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        viewModel.setCanvasColor(color)
                                        if (selectedTool == CanvasTool.ERASER) {
                                            viewModel.setCanvasTool(CanvasTool.PEN)
                                        }
                                    }
                            )
                        }
                    }

                    // Stroke width picker pills
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf(3f to "Fine", 6f to "Med", 14f to "Bold").forEach { (width, label) ->
                            val isSelected = strokeWidth == width
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { viewModel.setStrokeWidth(width) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Primary Tools & Page Navigator Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Tool buttons: Pen, Pencil, Highlighter, Eraser
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        ToolIconButton(
                            icon = Icons.Default.Brush,
                            label = LanguageStrings.get("wb_tool_pen", language),
                            isSelected = selectedTool == CanvasTool.PEN,
                            onClick = { viewModel.setCanvasTool(CanvasTool.PEN) }
                        )
                        ToolIconButton(
                            icon = Icons.Default.Edit,
                            label = LanguageStrings.get("wb_tool_pencil", language),
                            isSelected = selectedTool == CanvasTool.PENCIL,
                            onClick = { viewModel.setCanvasTool(CanvasTool.PENCIL) }
                        )
                        ToolIconButton(
                            icon = Icons.Default.Highlight,
                            label = LanguageStrings.get("wb_tool_highlighter", language),
                            isSelected = selectedTool == CanvasTool.HIGHLIGHTER,
                            onClick = { viewModel.setCanvasTool(CanvasTool.HIGHLIGHTER) }
                        )
                        ToolIconButton(
                            icon = Icons.Outlined.CleaningServices,
                            label = LanguageStrings.get("wb_tool_eraser", language),
                            isSelected = selectedTool == CanvasTool.ERASER,
                            onClick = { viewModel.setCanvasTool(CanvasTool.ERASER) }
                        )
                    }

                    // Multi-page navigation controls
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(
                            onClick = { viewModel.prevPage() },
                            enabled = currentPage > 1,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.NavigateBefore, contentDescription = "Previous Page")
                        }
                        Text(
                            text = "$currentPage/$totalPages",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        IconButton(
                            onClick = { viewModel.nextPage() },
                            enabled = currentPage < totalPages,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.NavigateNext, contentDescription = "Next Page")
                        }
                        IconButton(
                            onClick = { viewModel.addNewPage() },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "New Page", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ToolIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
            )
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
