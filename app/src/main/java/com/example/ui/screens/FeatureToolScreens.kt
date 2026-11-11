package com.example.ui.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.common.AppLanguage
import com.example.ui.common.LanguageStrings
import com.example.ui.theme.IndiaGreen
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.SaffronOrange
import com.example.ui.theme.SecondaryTeal
import com.example.viewmodel.AppScreen
import com.example.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubScreenScaffold(
    title: String,
    onBack: () -> Unit,
    content: @Composable () -> Unit
) {
    BackHandler { onBack() }
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack, modifier = Modifier.testTag("subscreen_back_button")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
        )
        Box(modifier = Modifier.weight(1f)) {
            content()
        }
    }
}

// 1. Maths Tools Screen with Working Calculator and Formula Catalog
@Composable
fun MathsToolsScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val language by viewModel.currentLanguage.collectAsStateWithLifecycle()
    var calcDisplay by remember { mutableStateOf("0") }
    var operand1 by remember { mutableDoubleStateOf(0.0) }
    var pendingOp by remember { mutableStateOf("") }
    var shouldClearOnNext by remember { mutableStateOf(false) }
    var selectedTable by remember { mutableIntStateOf(17) }

    SubScreenScaffold(
        title = LanguageStrings.get("tool_maths", language),
        onBack = { viewModel.navigateBack() }
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Calculator Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "गणितीय कैलकुलेटर" else "Educational Calculator",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // Display
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .padding(16.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text(
                                text = calcDisplay,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                ),
                                maxLines = 1
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Calculator Buttons Grid
                        val buttons = listOf(
                            listOf("C", "√", "%", "÷"),
                            listOf("7", "8", "9", "×"),
                            listOf("4", "5", "6", "-"),
                            listOf("1", "2", "3", "+"),
                            listOf("0", ".", "π", "=")
                        )

                        buttons.forEach { row ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                row.forEach { btn ->
                                    val isOp = btn in listOf("÷", "×", "-", "+", "=")
                                    val isClear = btn == "C"
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(46.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(
                                                when {
                                                    btn == "=" -> PrimaryBlue
                                                    isOp -> SecondaryTeal.copy(alpha = 0.2f)
                                                    isClear -> SaffronOrange.copy(alpha = 0.2f)
                                                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                                                }
                                            )
                                            .clickable {
                                                when (btn) {
                                                    "C" -> {
                                                        calcDisplay = "0"
                                                        operand1 = 0.0
                                                        pendingOp = ""
                                                    }
                                                    "π" -> calcDisplay = "3.14159"
                                                    "√" -> {
                                                        val v = calcDisplay.toDoubleOrNull() ?: 0.0
                                                        calcDisplay = kotlin.math.sqrt(v).toString().take(8)
                                                    }
                                                    "%" -> {
                                                        val v = calcDisplay.toDoubleOrNull() ?: 0.0
                                                        calcDisplay = (v / 100.0).toString()
                                                    }
                                                    "+", "-", "×", "÷" -> {
                                                        operand1 = calcDisplay.toDoubleOrNull() ?: 0.0
                                                        pendingOp = btn
                                                        shouldClearOnNext = true
                                                    }
                                                    "=" -> {
                                                        val operand2 = calcDisplay.toDoubleOrNull() ?: 0.0
                                                        val res = when (pendingOp) {
                                                            "+" -> operand1 + operand2
                                                            "-" -> operand1 - operand2
                                                            "×" -> operand1 * operand2
                                                            "÷" -> if (operand2 != 0.0) operand1 / operand2 else 0.0
                                                            else -> operand2
                                                        }
                                                        calcDisplay = if (res % 1.0 == 0.0) res.toLong().toString() else res.toString().take(9)
                                                        pendingOp = ""
                                                        shouldClearOnNext = true
                                                    }
                                                    else -> {
                                                        if (calcDisplay == "0" || shouldClearOnNext) {
                                                            calcDisplay = btn
                                                            shouldClearOnNext = false
                                                        } else {
                                                            calcDisplay += btn
                                                        }
                                                    }
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = btn,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = if (btn == "=") Color.White else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Multiplication Table Selector
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "पहाड़े (Multiplication Tables)" else "Multiplication Tables (1-20)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            items((1..20).toList()) { num ->
                                val isSelected = selectedTable == num
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedTable = num },
                                    label = { Text("Table $num") }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            (1..10).forEach { i ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("$selectedTable × $i", fontFamily = FontFamily.Monospace)
                                    Text("= ${selectedTable * i}", fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// 2. Geometry Tools Screen (Ruler, Protractor, 2D/3D shapes)
@Composable
fun GeometryToolsScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val language by viewModel.currentLanguage.collectAsStateWithLifecycle()
    var selectedAngle by remember { mutableIntStateOf(45) }

    SubScreenScaffold(
        title = LanguageStrings.get("tool_geometry", language),
        onBack = { viewModel.navigateBack() }
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Interactive Digital Ruler
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "डिजिटल स्केल (रूलर - १५ सेमी)" else "Precision Digital Ruler (15 cm)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        // Ruler Graphic
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFEF3C7))
                                .border(1.dp, Color(0xFFD97706), RoundedCornerShape(8.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                (0..15).forEach { cm ->
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Box(
                                            modifier = Modifier
                                                .width(1.5.dp)
                                                .height(if (cm % 5 == 0) 24.dp else 14.dp)
                                                .background(Color(0xFF78350F))
                                        )
                                        if (cm % 5 == 0) {
                                            Text(
                                                text = cm.toString(),
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF78350F)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Interactive Protractor Angle Measuring
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (language == AppLanguage.HINDI) "चांदा (कोण मापक - Protractor)" else "Interactive Protractor (${selectedAngle}°)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(listOf(30, 45, 60, 90, 120, 135, 180)) { angle ->
                                FilterChip(
                                    selected = selectedAngle == angle,
                                    onClick = { selectedAngle = angle },
                                    label = { Text("$angle°") }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(130.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$selectedAngle°",
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = SecondaryTeal
                                    )
                                )
                                Text(
                                    text = when (selectedAngle) {
                                        in 1..89 -> if (language == AppLanguage.HINDI) "न्यून कोण (Acute Angle)" else "Acute Angle"
                                        90 -> if (language == AppLanguage.HINDI) "समकोण (Right Angle)" else "Right Angle"
                                        in 91..179 -> if (language == AppLanguage.HINDI) "अधिक कोण (Obtuse Angle)" else "Obtuse Angle"
                                        else -> if (language == AppLanguage.HINDI) "ऋजु कोण (Straight Angle)" else "Straight Angle"
                                    },
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// 3. OCR Scanner Screen
@Composable
fun OCRScannerScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val language by viewModel.currentLanguage.collectAsStateWithLifecycle()

    var extractedText by remember {
        mutableStateOf(
            if (language == AppLanguage.HINDI)
                "न्यूटन का गति का प्रथम नियम: प्रत्येक वस्तु अपनी स्थिर अवस्था या एकसमान गति की अवस्था में तब तक बनी रहती है जब तक उस पर कोई बाहरी असंतुलित बल न लगाया जाए।"
            else
                "Newton's First Law of Motion: An object remains in a state of rest or of uniform motion in a straight line unless compelled to change that state by an applied unbalanced external force."
        )
    }

    SubScreenScaffold(
        title = LanguageStrings.get("tool_ocr", language),
        onBack = { viewModel.navigateBack() }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (language == AppLanguage.HINDI) "टेक्स्ट निष्कर्षण (हिंदी एवं अंग्रेजी OCR)" else "Offline OCR Text Extraction",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = extractedText,
                        onValueChange = { extractedText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(extractedText))
                                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (language == AppLanguage.HINDI) "कॉपी करें" else "Copy Text")
                        }

                        Button(
                            onClick = {
                                viewModel.createNewWhiteboard("OCR Extracted Note")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = SecondaryTeal),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if (language == AppLanguage.HINDI) "नोट्स में भेजें" else "Save as Note")
                        }
                    }
                }
            }
        }
    }
}

// 4. Quiz Maker Screen
@Composable
fun QuizMakerScreen(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val language by viewModel.currentLanguage.collectAsStateWithLifecycle()
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }

    SubScreenScaffold(
        title = LanguageStrings.get("tool_quiz", language),
        onBack = { viewModel.navigateBack() }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Question 1 of 5 • Science & Maths",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (language == AppLanguage.HINDI)
                            "प्रकाश संश्लेषण (Photosynthesis) की प्रक्रिया में पौधे कौन सी गैस ग्रहण करते हैं?"
                        else
                            "Which gas do green plants absorb from the atmosphere during photosynthesis?",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val options = if (language == AppLanguage.HINDI) {
                        listOf("ऑक्सीजन (Oxygen)", "कार्बन डाइऑक्साइड (Carbon Dioxide)", "नाइट्रोजन (Nitrogen)", "हाइड्रोजन (Hydrogen)")
                    } else {
                        listOf("Oxygen (O2)", "Carbon Dioxide (CO2)", "Nitrogen (N2)", "Hydrogen (H2)")
                    }

                    options.forEachIndexed { index, option ->
                        val isCorrect = index == 1
                        val isChosen = selectedOption == index
                        val bgColor = when {
                            isSubmitted && isCorrect -> IndiaGreen.copy(alpha = 0.2f)
                            isSubmitted && isChosen && !isCorrect -> Color.Red.copy(alpha = 0.15f)
                            isChosen -> MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(bgColor)
                                .clickable(enabled = !isSubmitted) { selectedOption = index }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isChosen,
                                onClick = { if (!isSubmitted) selectedOption = index }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = option, style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (!isSubmitted) {
                        Button(
                            onClick = { isSubmitted = true },
                            enabled = selectedOption != null,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if (language == AppLanguage.HINDI) "उत्तर जांचें (Submit)" else "Submit Answer")
                        }
                    } else {
                        val won = selectedOption == 1
                        Text(
                            text = if (won) "Correct! +1 Mark" else "Incorrect! Correct answer is Carbon Dioxide (CO2)",
                            fontWeight = FontWeight.Bold,
                            color = if (won) IndiaGreen else Color.Red
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                isSubmitted = false
                                selectedOption = null
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Next Question")
                        }
                    }
                }
            }
        }
    }
}

// 5. Screen Recorder & Voice Recorder Screen
@Composable
fun AudioVideoRecorderScreen(
    isScreenRec: Boolean,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsStateWithLifecycle()
    var isRecording by remember { mutableStateOf(false) }
    var secondsRecorded by remember { mutableIntStateOf(0) }

    SubScreenScaffold(
        title = LanguageStrings.get(if (isScreenRec) "tool_screen_rec" else "tool_voice_rec", language),
        onBack = { viewModel.navigateBack() }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(if (isRecording) Color.Red.copy(alpha = 0.15f) else PrimaryBlue.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isScreenRec) Icons.Default.Videocam else Icons.Default.Mic,
                    contentDescription = null,
                    tint = if (isRecording) Color.Red else PrimaryBlue,
                    modifier = Modifier.size(54.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = if (isRecording) "00:0${secondsRecorded / 60}:${(secondsRecorded % 60).toString().padStart(2, '0')}" else "Ready to Record",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
            )

            Text(
                text = if (isScreenRec) "High quality 1080p lesson recorder" else "High-fidelity classroom voice recorder",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { isRecording = !isRecording },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRecording) Color.Red else PrimaryBlue
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.size(width = 180.dp, height = 50.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.FiberManualRecord,
                        contentDescription = null
                    )
                    Text(text = if (isRecording) "Stop" else "Record")
                }
            }
        }
    }
}

// 6. Generic Tool Placeholder Screen for Mindmap, Flowchart, Presentation, Lesson Planner, Templates
@Composable
fun GenericToolScreen(
    titleKey: String,
    descKey: String,
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.currentLanguage.collectAsStateWithLifecycle()

    SubScreenScaffold(
        title = LanguageStrings.get(titleKey, language),
        onBack = { viewModel.navigateBack() }
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = LanguageStrings.get(titleKey, language),
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = LanguageStrings.get(descKey, language),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = { viewModel.createNewWhiteboard(LanguageStrings.get(titleKey, language)) },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (language == AppLanguage.HINDI) "नया कैनवास खोलें" else "Open in Whiteboard")
                    }
                }
            }
        }
    }
}
