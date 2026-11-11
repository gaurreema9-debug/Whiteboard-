package com.example.ui.common

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    ENGLISH("en", "English", "English"),
    HINDI("hi", "Hindi", "हिन्दी")
}

enum class AppThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

object LanguageStrings {
    fun get(key: String, language: AppLanguage): String {
        val strings = when (language) {
            AppLanguage.ENGLISH -> englishStrings
            AppLanguage.HINDI -> hindiStrings
        }
        return strings[key] ?: englishStrings[key] ?: key
    }

    private val englishStrings = mapOf(
        "app_name" to "Whiteboard India",
        "app_full_name" to "Whiteboard India Edition",
        "tagline" to "Learn • Draw • Teach",
        "nav_home" to "Home",
        "nav_whiteboard" to "Whiteboard",
        "nav_files" to "My Files",
        "nav_tools" to "Tools",
        "nav_settings" to "Settings",
        
        // Home Screen
        "search_hint" to "Search tools, boards, notes, lessons...",
        "quick_actions" to "Quick Actions",
        "new_whiteboard" to "New Whiteboard",
        "new_whiteboard_sub" to "Start fresh drawing or teaching canvas",
        "recent_files" to "Recently Opened",
        "see_all" to "See All",
        "categories" to "Feature Categories",
        "cat_all" to "All",
        "cat_whiteboards" to "Whiteboards",
        "cat_notes" to "Notes",
        "cat_pdf" to "PDF Tools",
        "cat_maths" to "Maths & STEM",
        "cat_teaching" to "Teaching",
        
        // 14 Core Tools
        "tool_new_wb" to "New Whiteboard",
        "tool_new_wb_desc" to "Blank, ruled, grid or green chalkboard",
        "tool_my_wb" to "My Whiteboards",
        "tool_my_wb_desc" to "Access all saved boards and lessons",
        "tool_pdf" to "PDF Tools",
        "tool_pdf_desc" to "Import, annotate, highlight & export PDFs",
        "tool_notes" to "My Notes",
        "tool_notes_desc" to "Handwritten & typed subject notes",
        "tool_maths" to "Maths Tools",
        "tool_maths_desc" to "Standard & scientific calculators & formulas",
        "tool_geometry" to "Geometry Tools",
        "tool_geometry_desc" to "Ruler, protractor, compass & 2D/3D shapes",
        "tool_ocr" to "OCR Scanner",
        "tool_ocr_desc" to "Image to text in Hindi and English",
        "tool_mindmap" to "Mind Map Builder",
        "tool_mindmap_desc" to "Visual concept maps with connected nodes",
        "tool_flowchart" to "Flowchart Builder",
        "tool_flowchart_desc" to "Standard process & decision diagrams",
        "tool_quiz" to "Quiz Maker",
        "tool_quiz_desc" to "Create MCQ & True/False quizzes with auto-scoring",
        "tool_presentation" to "Presentation Mode",
        "tool_presentation_desc" to "Full-screen slides with laser pointer",
        "tool_screen_rec" to "Screen Recorder",
        "tool_screen_rec_desc" to "Record lessons with whiteboard audio/video",
        "tool_voice_rec" to "Voice Recorder",
        "tool_voice_rec_desc" to "High-quality audio recording for lectures",
        "tool_planner" to "Lesson Planner",
        "tool_planner_desc" to "Subject syllabus, timetable & study planner",
        "tool_templates" to "Templates Gallery",
        "tool_templates_desc" to "50+ notebooks, graph sheets & exam templates",

        // Whiteboard Canvas
        "wb_tool_pen" to "Pen",
        "wb_tool_pencil" to "Pencil",
        "wb_tool_highlighter" to "Highlighter",
        "wb_tool_eraser" to "Eraser",
        "wb_tool_shapes" to "Shapes",
        "wb_tool_text" to "Text",
        "wb_tool_undo" to "Undo",
        "wb_tool_redo" to "Redo",
        "wb_tool_clear" to "Clear Canvas",
        "wb_tool_export" to "Export",
        "wb_bg_plain" to "Plain White",
        "wb_bg_ruled" to "Ruled Lines",
        "wb_bg_grid" to "Maths Grid",
        "wb_bg_graph" to "Graph Paper",
        "wb_bg_chalk" to "Green Chalkboard",
        "wb_bg_dark" to "Dark Slate",
        "wb_page" to "Page",
        "wb_saved" to "Autosaved locally",

        // Settings Screen
        "settings_title" to "Settings & Preferences",
        "settings_appearance" to "Appearance & Display",
        "settings_theme" to "Theme Mode",
        "settings_theme_sub" to "Light, Dark Navy, or System Default",
        "settings_language" to "Application Language",
        "settings_language_sub" to "Choose between English and Hindi",
        "settings_storage" to "Storage & Data",
        "settings_storage_sub" to "100% Offline-First. Stored on this device",
        "settings_admob" to "Google AdMob Status",
        "settings_admob_sub" to "Non-intrusive test ads enabled (Free model)",
        "settings_about" to "About Whiteboard India Edition",
        "settings_version" to "Version 1.0.0 (Build 1)",
        "settings_free_guarantee" to "All Core Educational Features Are Completely Free",
        "privacy_policy" to "Privacy Policy & Zero Data Collection",

        // AdMob Test Notice
        "ad_banner_label" to "Google AdMob Test Banner • Non-intrusive Safe Ad",
        "ad_banner_id" to "Ad Unit: ca-app-pub-3940256099942544/6300978111"
    )

    private val hindiStrings = mapOf(
        "app_name" to "व्हाइटबोर्ड इंडिया",
        "app_full_name" to "व्हाइटबोर्ड इंडिया संस्करण",
        "tagline" to "सीखें • लिखें • सिखाएं",
        "nav_home" to "होम",
        "nav_whiteboard" to "व्हाइटबोर्ड",
        "nav_files" to "मेरी फाइलें",
        "nav_tools" to "उपकरण",
        "nav_settings" to "सेटिंग्स",

        // Home Screen
        "search_hint" to "टूल्स, बोर्ड, नोट्स और पाठ खोजें...",
        "quick_actions" to "त्वरित क्रियाएँ",
        "new_whiteboard" to "नया व्हाइटबोर्ड",
        "new_whiteboard_sub" to "नया शिक्षण या ड्राइंग कैनवास शुरू करें",
        "recent_files" to "हाल ही में खोले गए",
        "see_all" to "सभी देखें",
        "categories" to "सुविधा श्रेणियां",
        "cat_all" to "सभी",
        "cat_whiteboards" to "व्हाइटबोर्ड",
        "cat_notes" to "नोट्स",
        "cat_pdf" to "पीडीएफ टूल्स",
        "cat_maths" to "गणित व विज्ञान",
        "cat_teaching" to "अध्यापन",

        // 14 Core Tools
        "tool_new_wb" to "नया व्हाइटबोर्ड",
        "tool_new_wb_desc" to "सादा, रेखादार, ग्रिड या हरा ब्लैकबोर्ड",
        "tool_my_wb" to "मेरे व्हाइटबोर्ड",
        "tool_my_wb_desc" to "सहेजे गए सभी बोर्ड और पाठ देखें",
        "tool_pdf" to "पीडीएफ उपकरण",
        "tool_pdf_desc" to "पीडीएफ आयात करें, लिखें और साझा करें",
        "tool_notes" to "मेरे नोट्स",
        "tool_notes_desc" to "हस्तलिखित और टाइप किए गए नोट्स",
        "tool_maths" to "गणित उपकरण",
        "tool_maths_desc" to "वैज्ञानिक कैलकुलेटर और सूत्र संग्रह",
        "tool_geometry" to "ज्यामिति उपकरण",
        "tool_geometry_desc" to "स्केल, चांदा, परकार और ज्यामितीय आकृतियाँ",
        "tool_ocr" to "ओसीआर स्कैनर",
        "tool_ocr_desc" to "हिंदी और अंग्रेजी फोटो से टेक्स्ट निकालें",
        "tool_mindmap" to "माइंड मैप निर्माता",
        "tool_mindmap_desc" to "विचारों को आरेख से जोड़कर समझें",
        "tool_flowchart" to "फ्लोचार्ट निर्माता",
        "tool_flowchart_desc" to "प्रक्रिया व निर्णय आरेख बनाएं",
        "tool_quiz" to "प्रश्नोत्तरी निर्माता",
        "tool_quiz_desc" to "बहुविकल्पीय प्रश्नोत्तरी व त्वरित परिणाम",
        "tool_presentation" to "प्रस्तुति मोड",
        "tool_presentation_desc" to "लेजर पॉइंटर के साथ फुल स्क्रीन शिक्षण",
        "tool_screen_rec" to "स्क्रीन रिकॉर्डर",
        "tool_screen_rec_desc" to "व्हाइटबोर्ड पर पढ़ाते हुए वीडियो रिकॉर्ड करें",
        "tool_voice_rec" to "आवाज रिकॉर्डर",
        "tool_voice_rec_desc" to "पाठ और व्याख्यान की उच्च गुणवत्ता रिकॉर्डिंग",
        "tool_planner" to "पाठ योजनाकार",
        "tool_planner_desc" to "समय सारणी और पाठ्यक्रम योजना बनाएं",
        "tool_templates" to "टेम्प्लेट गैलरी",
        "tool_templates_desc" to "५०+ अभ्यास पत्र, ग्रिड और नोटबुक",

        // Whiteboard Canvas
        "wb_tool_pen" to "कलम",
        "wb_tool_pencil" to "पेंसिल",
        "wb_tool_highlighter" to "हाइलाइटर",
        "wb_tool_eraser" to "रबर",
        "wb_tool_shapes" to "आकृतियां",
        "wb_tool_text" to "टेक्स्ट",
        "wb_tool_undo" to "पूर्ववत",
        "wb_tool_redo" to "दोहराएं",
        "wb_tool_clear" to "कैनवास साफ़ करें",
        "wb_tool_export" to "निर्यात",
        "wb_bg_plain" to "सफ़ेद बोर्ड",
        "wb_bg_ruled" to "रेखादार",
        "wb_bg_grid" to "गणित ग्रिड",
        "wb_bg_graph" to "ग्राफ पेपर",
        "wb_bg_chalk" to "हरा ब्लैकबोर्ड",
        "wb_bg_dark" to "डार्क स्लेट",
        "wb_page" to "पृष्ठ",
        "wb_saved" to "सुरक्षित सहेजा गया",

        // Settings Screen
        "settings_title" to "सेटिंग्स और प्राथमिकताएं",
        "settings_appearance" to "दिखावट एवं थीम",
        "settings_theme" to "थीम मोड",
        "settings_theme_sub" to "लाइट, नेवी डार्क या सिस्टम डिफॉल्ट",
        "settings_language" to "एप्लिकेशन की भाषा",
        "settings_language_sub" to "अंग्रेजी या हिन्दी का चयन करें",
        "settings_storage" to "डेटा और भंडारण",
        "settings_storage_sub" to "१००% ऑफ़लाइन-प्रथम। इसी उपकरण में सुरक्षित",
        "settings_admob" to "गूगल ऐडमॉब स्थिति",
        "settings_admob_sub" to "सुरक्षित गैर-दखलंदाजी परीक्षण विज्ञापन",
        "settings_about" to "व्हाइटबोर्ड इंडिया के बारे में",
        "settings_version" to "संस्करण १.०.० (बिल्ड १)",
        "settings_free_guarantee" to "सभी प्रमुख शैक्षणिक सुविधाएं हमेशा निःशुल्क हैं",
        "privacy_policy" to "गोपनीयता नीति • शून्य डेटा संग्रह",

        // AdMob Test Notice
        "ad_banner_label" to "गूगल ऐडमॉब परीक्षण बैनर • सुरक्षित विज्ञापन",
        "ad_banner_id" to "विज्ञापन इकाई: ca-app-pub-3940256099942544/6300978111"
    )
}
