package com.catcompanion.app.ui.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.catcompanion.app.data.local.entity.ChatMessageEntity
import com.catcompanion.app.data.local.entity.SenderType
import com.catcompanion.app.data.viewmodel.ChatViewModel
import com.catcompanion.app.ui.theme.*

/**
 * AI 聊天页 Composable
 * 气泡式聊天界面，区分用户消息与AI回复
 * 底部输入框支持发送消息
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    val messages by lazy { viewModel.messages.toList() }
    val selectedCat by lazy { viewModel.selectedCat }
    val isLoading by lazy { viewModel.isLoading }

    // 懒加载列表状态 - 用于滚动到最新消息
    val listState = rememberLazyListState()

    // 当消息更新时自动滚动到底部
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // 猫咪头像小图标
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(50))
                                .background(getCatColor(selectedCat?.id)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = getCatEmoji(selectedCat?.id),
                                fontSize = 20.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(selectedCat?.displayName ?: "猫咪伙伴")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    // 清空历史按钮 - 调用 /clear_history 接口
                    IconButton(onClick = {
                        viewModel.clearHistory()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "清空对话")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                )
            )
        },
        bottomBar = {
            // 底部输入栏
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    // 文本输入框
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = {
                            Text("和${selectedCat?.displayName}说点什么吧...", style = MaterialTheme.typography.bodyMedium)
                        },
                        modifier = Modifier.weight(1f),
                        maxLines = 4,
                        shape = RoundedCornerShape(20.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            containerColor = LightBackground
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // 发送按钮
                    FloatingActionButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                viewModel.sendMessage(inputText)
                                inputText = ""
                            }
                        },
                        modifier = Modifier.size(48.dp),
                        containerColor = PrimaryOrange
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "发送", tint = Color.White)
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            // 聊天消息列表
            if (messages.isEmpty()) {
                // 空状态提示
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "还没有聊天记录哦~\n开始和${selectedCat?.displayName}聊天吧！",
                        style = MaterialTheme.typography.bodyLarge,
                        color = LightText,
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    )
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(messages, key = { it.id }) { message ->
                        MessageBubble(message)
                    }

                    // 加载中指示器
                    if (isLoading) {
                        item {
                            LoadingIndicator()
                        }
                    }
                }
            }
        }
    }
}

/**
 * 单条聊天消息气泡
 * 用户消息显示在右侧（蓝色），AI 消息显示在左侧（灰色）
 */
@Composable
private fun MessageBubble(message: ChatMessageEntity) {
    val isUser = message.sender == SenderType.USER
    val bubbleColor = if (isUser) PrimaryOrange else SurfaceColor

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        // AI 消息左侧显示头像
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(50))
                    .background(SurfaceColor.copy(alpha = 0.8f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🐱", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        // 消息气泡
        Card(
            modifier = Modifier.widthIn(max = 260.dp),
            shape = RoundedCornerShape(
                topStart = if (isUser) 16.dp else 16.dp,
                topEnd = 16.dp,
                bottomStart = 16.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = bubbleColor
            )
        ) {
            Text(
                text = message.message,
                modifier = Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isUser) Color.White else DarkText,
                lineHeight = 22.sp
            )
        }

        // 用户消息右侧显示占位头像
        if (isUser) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(50))
                    .background(PrimaryOrange.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "😊", fontSize = 16.sp)
            }
        }
    }
}

/**
 * 加载指示器 - AI 正在思考中...
 */
@Composable
private fun LoadingIndicator() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(50))
                .background(SurfaceColor.copy(alpha = 0.8f)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🐱", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.width(8.dp))
        // 三个跳动的小圆点表示正在思考
        LoadingDots()
    }
}

/**
 * 跳动的省略号动画
 */
@Composable
private fun LoadingDots() {
    val dots = listOf(".", "..", "...")
    var currentIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(500L)
            currentIndex = (currentIndex + 1) % dots.size
        }
    }

    Text(
        text = dots[currentIndex],
        style = MaterialTheme.typography.bodyMedium,
        color = LightText
    )
}

/**
 * 根据猫咪ID获取颜色
 */
private fun getCatColor(petId: String?): Color {
    return when (petId) {
        "fubao" -> FubaoColor
        "buding" -> BudingColor
        "xixi" -> XixiColor
        "paofu" -> PaofuColor
        else -> PrimaryOrange
    }
}

/**
 * 根据猫咪ID获取表情符号
 */
private fun getCatEmoji(petId: String?): String {
    return when (petId) {
        "fubao" -> "🐱"
        "buding" -> "😸"
        "xixi" -> "😻"
        "paofu" -> "😼"
        else -> "🐾"
    }
}
