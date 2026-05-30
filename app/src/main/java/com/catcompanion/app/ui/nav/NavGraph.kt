package com.catcompanion.app.ui.nav

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.catcompanion.app.data.local.entity.StoryEntity
import com.catcompanion.app.ui.home.HomeScreen
import com.catcompanion.app.ui.chat.ChatScreen
import com.catcompanion.app.ui.onboarding.OnboardingScreen
import com.catcompanion.app.ui.story.StoryDetailScreen
import com.catcompanion.app.ui.story.StoryListScreen

/**
 * App 导航路由定义
 * 管理所有页面的跳转逻辑
 */

// ===== 路由常量 =====
object Routes {
    const val ONBOARDING = "onboarding"        // 引导页
    const val HOME = "home"                    // 首页
    const val CHAT = "chat"                    // 聊天页
    const val STORY_LIST = "story_list"        // 故事列表页
    const val STORY_DETAIL = "story_detail"    // 故事详情页
}

/**
 * 初始化 NavHostController
 */
fun createNavController(): NavHostController {
    return NavHostController()
}

/**
 * 配置导航图
 * @param navController 导航控制器
 * @param startDestination 起始页面（根据是否已选猫咪决定）
 */
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    startDestination: String = Routes.HOME
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ===== 引导页 =====
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onCompleted = { cat ->
                    navController.navigate("${Routes.HOME}?selectedCat=${cat.id}") {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        // ===== 首页 =====
        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToChat = {
                    navController.navigate(Routes.CHAT)
                },
                onNavigateToStory = {
                    navController.navigate("${Routes.STORY_LIST}?petId=fubao&petName=福宝")
                },
                onChangeCat = {
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(Routes.HOME) { inclusive = false }
                    }
                }
            )
        }

        // ===== 聊天页 =====
        composable(Routes.CHAT) {
            ChatScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // ===== 故事列表页（带参数：petId, petName） =====
        composable(
            route = "${Routes.STORY_LIST}?petId={petId}&petName={petName}",
            arguments = listOf(
                navArgument("petId") { type = NavType.StringType },
                navArgument("petName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val petId = backStackEntry.arguments?.getString("petId") ?: "fubao"
            val petName = backStackEntry.arguments?.getString("petName") ?: "福宝"

            StoryListScreen(
                petId = petId,
                petName = petName,
                onBack = { navController.popBackStack() },
                onOpenStory = { story ->
                    navController.navigate("${Routes.STORY_DETAIL}?storyId=${story.id}&chapterTitle=${story.chapterTitle}")
                }
            )
        }

        // ===== 故事详情页（带参数：storyId, chapterTitle） =====
        composable(
            route = "${Routes.STORY_DETAIL}?storyId={storyId}&chapterTitle={chapterTitle}",
            arguments = listOf(
                navArgument("storyId") { type = NavType.StringType },
                navArgument("chapterTitle") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val storyId = backStackEntry.arguments?.getString("storyId") ?: ""
            val chapterTitle = backStackEntry.arguments?.getString("chapterTitle") ?: ""

            // 根据 storyId 查找对应 StoryEntity
            val story = getStoryById(storyId, chapterTitle)

            if (story != null) {
                StoryDetailScreen(
                    story = story,
                    onBack = { navController.popBackStack() }
                )
            } else {
                Text("故事未找到")
            }
        }
    }
}

/**
 * 根据 storyId 从模拟数据中查找 StoryEntity
 * （开发阶段使用本地数据，生产环境替换为 API 调用）
 */
private fun getStoryById(id: String, chapterTitle: String): StoryEntity? {
    val repository = com.catcompanion.app.data.repository.AppRepository()
    val allPets = listOf("fubao", "buding", "xixi", "paofu")
    for (petId in allPets) {
        val stories = repository.getStoriesByPet(petId)
        stories.find { it.id == id }?.let { return it }
    }
    return null
}
