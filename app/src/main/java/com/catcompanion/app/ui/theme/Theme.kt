package com.catcompanion.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * 浅色主题颜色方案
 */
private val LightColorScheme = lightColorScheme(
    primary = PrimaryOrange,
    onPrimary = Color.White,
    secondary = SecondaryColor,
    tertiary = AccentPink,
    background = LightBackground,
    surface = LightSurface,
    onSurface = DarkText,
    onBackground = DarkText,
    primaryContainer = PrimaryOrangeLight,
    surfaceVariant = DividerColor
)

/**
 * 深色主题颜色方案（预留）
 */
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Color.Black,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF1A1A2E),
    surface = Color(0xFF2D2D44),
    onSurface = Color.White,
    onBackground = Color.White
)

/**
 * 主题 Composable - 应用全局 Material3 配色
 */
@Composable
fun CatCompanionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // 根据系统主题选择配色方案
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    // 配置状态栏文字颜色为深色（浅主题下）
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
