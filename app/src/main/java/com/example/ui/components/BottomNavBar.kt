package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Draw
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.ui.common.AppLanguage
import com.example.ui.common.LanguageStrings
import com.example.viewmodel.AppScreen

data class NavItem(
    val screen: AppScreen,
    val labelKey: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val tag: String
)

@Composable
fun BottomNavBar(
    currentScreen: AppScreen,
    language: AppLanguage,
    onTabSelected: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem(
            screen = AppScreen.HOME,
            labelKey = "nav_home",
            selectedIcon = Icons.Filled.Dashboard,
            unselectedIcon = Icons.Outlined.Dashboard,
            tag = "bottom_nav_home"
        ),
        NavItem(
            screen = AppScreen.WHITEBOARD,
            labelKey = "nav_whiteboard",
            selectedIcon = Icons.Filled.Draw,
            unselectedIcon = Icons.Outlined.Draw,
            tag = "bottom_nav_whiteboard"
        ),
        NavItem(
            screen = AppScreen.MY_FILES,
            labelKey = "nav_files",
            selectedIcon = Icons.Filled.Description,
            unselectedIcon = Icons.Outlined.Description,
            tag = "bottom_nav_files"
        ),
        NavItem(
            screen = AppScreen.TOOLS,
            labelKey = "nav_tools",
            selectedIcon = Icons.Filled.Build,
            unselectedIcon = Icons.Outlined.Build,
            tag = "bottom_nav_tools"
        ),
        NavItem(
            screen = AppScreen.SETTINGS,
            labelKey = "nav_settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            tag = "bottom_nav_settings"
        )
    )

    NavigationBar(
        modifier = modifier.testTag("bottom_navigation_bar"),
        windowInsets = WindowInsets.navigationBars,
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    ) {
        items.forEach { item ->
            val isSelected = currentScreen == item.screen
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(item.screen) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = LanguageStrings.get(item.labelKey, language)
                    )
                },
                label = {
                    Text(
                        text = LanguageStrings.get(item.labelKey, language),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag(item.tag)
            )
        }
    }
}
