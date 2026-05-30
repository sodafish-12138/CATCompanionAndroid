package com.catcompanion.app.ui.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.catcompanion.app.ui.nav.Routes
import com.catcompanion.app.ui.nav.SetupNavGraph
import com.catcompanion.app.ui.theme.CatCompanionTheme

/**
 * 主 Activity - App 入口点
 * 负责初始化导航和主题
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CatCompanionTheme {
                val navController = rememberNavController()

                // 根据 intent extras 判断是否为首次启动
                val startDestination = if (intent.getBooleanExtra("FIRST_LAUNCH", true)) {
                    Routes.ONBOARDING  // 首次启动进入引导页
                } else {
                    Routes.HOME        // 后续启动直接进入首页
                }

                SetupNavGraph(
                    navController = navController,
                    startDestination = startDestination
                )
            }
        }
    }
}
