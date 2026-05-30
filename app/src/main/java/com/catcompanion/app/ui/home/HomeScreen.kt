package com.catcompanion.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.catcompanion.app.data.local.entity.CatEntity
import com.catcompanion.app.data.viewmodel.HomeViewModel
import com.catcompanion.app.ui.theme.*

/**
 * 首页 Composable
 * 展示已选中的猫咪头像和名称，提供聊天和故事两个核心入口
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToChat: () -> Unit,
    onNavigateToStory: () -> Unit,
    onChangeCat: () -> Unit
) {
    // 初始化时加载已选中的猫咪信息
    LaunchedEffect(Unit) {
        viewModel.initSelectedCat()
    }

    val selectedCat by lazy { viewModel.selectedCat }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("🐱 猫咪陪伴")
                },
                actions = {
                    // 更换猫咪按钮
                    IconButton(onClick = onChangeCat) {
                        Icon(Icons.Default.Refresh, contentDescription = "更换猫咪")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(LightBackground),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // ===== 猫咪形象展示区 =====
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 猫咪头像 - 使用 Coil 加载网络图片（开发阶段用占位符）
                    selectedCat?.let { cat ->
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Brush.radialGradient(
                                    colors = listOf(getCatGradient(cat.id).copy(alpha = 0.6f), getCatGradient(cat.id).copy(alpha = 0.2f))
                                )),
                            contentAlignment = Alignment.Center
                        ) {
                            val emoji = when (cat.id) {
                                "fubao" -> "🐱"
                                "buding" -> "😸"
                                "xixi" -> "😻"
                                "paofu" -> "😼"
                                else -> "🐾"
                            }
                            Text(text = emoji, fontSize = 64.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 猫咪名字
                        Text(
                            text = "${cat.displayName}的陪伴时光",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = DarkText
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // 性格描述
                        Text(
                            text = cat.personality,
                            style = MaterialTheme.typography.bodyMedium,
                            color = LightText,
                            textAlign = TextAlign.Center
                        )
                    } ?: run {
                        Text(
                            text = "还没有选择猫咪伙伴哦~\n请在引导页选择一只猫咪开始吧",
                            style = MaterialTheme.typography.bodyLarge,
                            color = LightText,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ===== 核心功能入口 =====
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // 聊天入口卡片
                FeatureCard(
                    icon = Icons.Default.Chat,
                    title = "AI 聊天",
                    description = "和猫咪伙伴说说话\n分享你的心情与想法",
                    gradientColors = listOf(FubaoColor.copy(alpha = 0.8f), FubaoColor.copy(alpha = 0.4f)),
                    onClick = onNavigateToChat
                )

                // 故事入口卡片
                FeatureCard(
                    icon = Icons.Default.MenuBook,
                    title = "专属故事",
                    description = "聆听猫咪的第一人称\n温馨治愈的冒险旅程",
                    gradientColors = listOf(AccentPink.copy(alpha = 0.8f), AccentPink.copy(alpha = 0.4f)),
                    onClick = onNavigateToStory
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // 底部版权信息
            Text(
                text = "Made with ❤️ for Cat Lovers",
                style = MaterialTheme.typography.bodySmall,
                color = LightText.copy(alpha = 0.5f),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

/**
 * 功能入口卡片
 * @param icon 图标资源
 * @param title 标题文字
 * @param description 描述文字
 * @param gradientColors 渐变颜色
 * @param onClick 点击事件回调
 */
@Composable
private fun FeatureCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    gradientColors: List<Color>,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(200.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 图标背景
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(gradientColors)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 标题
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DarkText
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 描述
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = LightText,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}

/**
 * 根据猫咪ID获取代表色
 */
private fun getCatGradient(petId: String): Color {
    return when (petId) {
        "fubao" -> FubaoColor
        "buding" -> BudingColor
        "xixi" -> XixiColor
        "paofu" -> PaofuColor
        else -> PrimaryOrange
    }
}
