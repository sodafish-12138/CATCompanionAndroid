package com.catcompanion.app.ui.story

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.catcompanion.app.data.local.entity.StoryEntity
import com.catcompanion.app.data.viewmodel.StoryViewModel
import com.catcompanion.app.ui.theme.*

/**
 * 故事列表页 Composable
 * 展示当前猫咪的10章故事章节
 * 第1-7章可正常点击，第8-10章显示锁图标、禁止点击
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryListScreen(
    viewModel: StoryViewModel = hiltViewModel(),
    petId: String,
    petName: String,
    onBack: () -> Unit,
    onOpenStory: (StoryEntity) -> Unit
) {
    // 加载指定猫咪的故事列表
    LaunchedEffect(petId) {
        viewModel.loadStories(petId)
    }

    val stories by lazy { viewModel.stories }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("$petName的故事") },
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
        if (stories.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "故事加载中...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = LightText
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(stories, key = { it.id }) { story ->
                    StoryItemCard(
                        story = story,
                        onClick = { onOpenStory(story) }
                    )
                }
            }
        }
    }
}

/**
 * 单章故事卡片
 * 付费章节（8-10）显示锁图标和锁定提示
 */
@Composable
private fun StoryItemCard(
    story: StoryEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = !story.isLocked, onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (story.isLocked)
                Color.LightGray.copy(alpha = 0.3f)
            else
                Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (story.isLocked) 0.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 章节序号 - 圆形背景
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (story.isLocked)
                            Color.Gray.copy(alpha = 0.5f)
                        else
                            Brush.linearGradient(listOf(PrimaryOrange, AccentPink))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = story.chapterNumber.toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (story.isLocked) Color.DarkGray else Color.White
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // 章节标题
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = story.chapterTitle.replaceFirst("第.*章 ", ""),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (story.isLocked) Color.DarkGray.copy(alpha = 0.5f) else DarkText
                )

                if (story.isLocked) {
                    Text(
                        text = "付费章节 - 解锁后阅读",
                        style = MaterialTheme.typography.bodySmall,
                        color = AccentPink
                    )
                } else {
                    Text(
                        text = "共 ${story.content.length} 字",
                        style = MaterialTheme.typography.bodySmall,
                        color = LightText
                    )
                }
            }

            // 右侧状态图标
            if (story.isLocked) {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = "付费锁定",
                    tint = Color.Gray.copy(alpha = 0.6f),
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = "查看详情",
                    tint = LightText,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
