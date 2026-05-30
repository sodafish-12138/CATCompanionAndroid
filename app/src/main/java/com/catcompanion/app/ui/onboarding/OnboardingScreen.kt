package com.catcompanion.app.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import coil.compose.AsyncImage
import com.catcompanion.app.data.local.entity.CatEntity
import com.catcompanion.app.data.viewmodel.OnboardingViewModel
import com.catcompanion.app.ui.theme.*

/**
 * 引导页数据模型 - 每页的内容
 */
data class OnboardingPage(
    val title: String,          // 页面标题
    val description: String,    // 页面描述文字
    val imageUrl: String,       // 图片URL（用于Coil加载）
    val backgroundColors: List<Color>  // 渐变背景色
)

/**
 * 引导页内容列表 - 共6页
 */
val onboardingPages = listOf(
    OnboardingPage(
        title = "欢迎来到猫咪世界",
        description = "这里有4只性格各异的猫咪，\n选择一只成为你的专属陪伴伙伴吧~",
        imageUrl = "",
        backgroundColors = listOf(Color(0xFFFFF5EE), Color(0xFFFFE4D6))
    ),
    OnboardingPage(
        title = "福宝",
        description = "绅士稳重的大哥哥\n温柔可靠 · 暖心贴心",
        imageUrl = "",
        backgroundColors = listOf(Color(0xFFE8F0FE), Color(0xFFC5D9F0))
    ),
    OnboardingPage(
        title = "小布丁",
        description = "阳光帅气的元气弟弟\n热情开朗 · 活力四射",
        imageUrl = "",
        backgroundColors = listOf(Color(0xFFFFF3E0), Color(0xFFFFE0B2))
    ),
    OnboardingPage(
        title = "茜茜",
        description = "温柔细腻的知心姐姐\n优雅贴心 · 善解人意",
        imageUrl = "",
        backgroundColors = listOf(Color(0xFFFCE4EC), Color(0xFFF8BBD0))
    ),
    OnboardingPage(
        title = "泡芙",
        description = "傲娇元气的大小姐\n嘴硬心软 · 活泼可爱",
        imageUrl = "",
        backgroundColors = listOf(Color(0xFFFFEBEE), Color(0xFFFFCDD2))
    ),
    OnboardingPage(
        title = "做出你的选择",
        description = "点击下方的猫咪头像进行选择\n准备好了吗？开始你们的陪伴之旅吧！",
        imageUrl = "",
        backgroundColors = listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7))
    )
)

/**
 * 引导页 Composable
 * 6页可左右滑动式引导页，最后一展示4只猫咪供用户选择
 * @param onCompleted 完成引导后的回调，传入选中的猫咪对象
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onCompleted: (CatEntity) -> Unit
) {
    val pagerState = rememberPagerState(
        pageCount = { onboardingPages.size }
    )
    var showCatSelection by mutableStateOf(false)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {}, // 无标题
                navigationIcon = {
                    if (pagerState.currentPage > 0) {
                        IconButton(onClick = {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "返回上一页"
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(56.dp))
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
        ) {
            // 翻页指示器 - 页面底部的小圆点
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(onboardingPages.size) { index ->
                    val isSelected = index == pagerState.currentPage
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (isSelected) 10.dp else 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) PrimaryOrange else Color.Gray.copy(alpha = 0.3f)
                            )
                    )
                }
            }

            // 主内容区 - HorizontalPager 实现左右滑动
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                PageContent(
                    page = onboardingPages[page],
                    currentPage = pagerState.currentPage,
                    lastPage = pagerState.pageCount - 1
                )
            }

            // 底部按钮区域
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 跳过按钮（仅前几页显示）
                if (pagerState.currentPage < onboardingPages.size - 1) {
                    TextButton(
                        onClick = {
                            showCatSelection = true
                        }
                    ) {
                        Text("跳过")
                    }
                }

                // 下一步 / 开始按钮
                Button(
                    onClick = {
                        if (pagerState.currentPage < onboardingPages.last) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        } else {
                            // 最后一页：跳转到猫咪选择页
                            showCatSelection = true
                        }
                    },
                    modifier = Modifier.padding(start = 16.dp)
                ) {
                    Text(if (pagerState.currentPage < onboardingPages.last) "下一步" else "开始")
                }
            }

            // 猫咪选择面板（最后一页时显示）
            if (showCatSelection) {
                CatSelectionPanel(
                    cats = viewModel.cats,
                    onSelect = { cat ->
                        viewModel.selectCat(cat)
                        onCompleted(cat)
                    }
                )
            }
        }
    }
}

/**
 * 单页内容 Composable
 * 每页包含标题、描述和占位图（可用 Coil 加载真实图片）
 */
@Composable
private fun PageContent(
    page: OnboardingPage,
    currentPage: Int,
    lastPage: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 标题 - 大号加粗字体
        Text(
            text = page.title,
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
            color = DarkText
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 描述文字 - 居中排列
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.center,
            color = LightText,
            lineHeight = 28.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 图片占位符（实际开发中用 Coil 加载真实图片）
        ImagePlaceholder(
            color = when (currentPage) {
                1 -> FubaoColor
                2 -> BudingColor
                3 -> XixiColor
                4 -> PaofuColor
                else -> PrimaryOrange
            },
            emoji = when (currentPage) {
                1 -> "🐱"
                2 -> "😸"
                3 -> "😻"
                4 -> "😼"
                else -> "🏠"
            }
        )
    }
}

/**
 * 图片占位组件
 * 开发阶段使用 Emoji 作为头像占位，生产环境替换为 Coil.asyncImage
 * @param color 代表色
 * @param emoji 猫咪表情符号作为占位图
 */
@Composable
private fun ImagePlaceholder(
    color: Color,
    emoji: String
) {
    Box(
        modifier = Modifier
            .size(160.dp)
            .clip(CircleShape)
            .background(Brush.radialGradient(
                colors = listOf(color.copy(alpha = 0.8f), color.copy(alpha = 0.3f))
            )),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = emoji,
            fontSize = 80.sp
        )
    }
}

/**
 * 猫咪选择面板 - 4只猫咪并排展示，点击选中
 */
@Composable
fun CatSelectionPanel(
    cats: List<CatEntity>,
    onSelect: (CatEntity) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .animateEnterExit(),
        color = Color.White.copy(alpha = 0.95f),
        tonalElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "选择你的专属猫咪伙伴",
                style = MaterialTheme.typography.titleLarge,
                color = DarkText
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 4只猫咪卡片横向排列
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                cats.forEach { cat ->
                    CatCard(cat = cat, onClick = onSelect)
                }
            }
        }
    }
}

/**
 * 单只猫咪卡片
 */
@Composable
private fun CatCard(
    cat: CatEntity,
    onClick: (CatEntity) -> Unit
) {
    val color = when (cat.id) {
        "fubao" -> FubaoColor
        "buding" -> BudingColor
        "xixi" -> XixiColor
        "paofu" -> PaofuColor
        else -> Color.Gray
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick(cat) }
            .padding(8.dp)
    ) {
        // 猫咪头像圆形占位
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(Brush.radialGradient(
                    colors = listOf(color.copy(alpha = 0.6f), color.copy(alpha = 0.2f))
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
            Text(text = emoji, fontSize = 40.sp)
        }

        Spacer(modifier = Modifier.height(4.dp))

        // 猫咪名称
        Text(
            text = cat.displayName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = DarkText
        )

        // 性格简述
        Text(
            text = cat.personality.split("的")[0] + "的",
            style = MaterialTheme.typography.bodySmall,
            color = LightText
        )
    }
}
