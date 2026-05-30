package com.catcompanion.app.ui.story

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.catcompanion.app.data.local.entity.StoryEntity
import com.catcompanion.app.ui.theme.*

/**
 * 故事详情页 Composable
 * 展示选中章节的完整正文内容，支持垂直滚动阅读
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryDetailScreen(
    story: StoryEntity,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(story.chapterTitle) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(LightBackground),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 标题区域
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = story.chapterTitle,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Divider(color = DividerColor)

                    Spacer(modifier = Modifier.height(8.dp))

                    // 章节信息
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (story.isLocked) {
                            Chip(
                                onClick = {},
                                enabled = false,
                                border = null,
                                colors = ChipDefaults.chipColors(
                                    containerColor = AccentPink.copy(alpha = 0.1f),
                                    contentColor = AccentPink
                                )
                            ) {
                                Text("🔒 付费章节")
                            }
                        } else {
                            Chip(
                                onClick = {},
                                colors = ChipDefaults.chipColors(
                                    containerColor = PrimaryOrange.copy(alpha = 0.1f),
                                    contentColor = PrimaryOrange
                                )
                            ) {
                                Text("✅ 已解锁")
                            }
                        }

                        Chip(
                            onClick = {},
                            colors = ChipDefaults.chipColors(
                                containerColor = LightBackground,
                                contentColor = LightText
                            )
                        ) {
                            Text("${story.content.length} 字")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 故事正文 - 分段显示
            Text(
                text = story.content,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = DarkText,
                lineHeight = 28.sp,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 底部装饰线
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 24.dp),
                thickness = 1.dp,
                color = DividerColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 底部提示
            Text(
                text = "— 完 —",
                style = MaterialTheme.typography.bodyMedium,
                color = LightText
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
