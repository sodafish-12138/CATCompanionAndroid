# 猫咪陪伴 App - CatCompanion

AI宠物陪伴安卓App，主打拟人AI聊天 + 专属猫咪故事 + 付费解锁高级内容。

## 📱 项目特色

- **4只个性猫咪**：福宝（绅士稳重）、小布丁（阳光活力）、茜茜（温柔细腻）、泡芙（傲娇元气）
- **6页引导页**：左右滑动式引导 + 猫咪选择界面
- **AI聊天**：气泡式聊天界面，切换角色自动清空上下文
- **专属故事**：每只猫咪10章第一人称叙事故事，前7章免费、后3章付费锁定
- **完整技术栈**：Kotlin + Jetpack Compose + Room + Retrofit + Coil

## 🛠 技术栈

| 类别 | 技术 |
|------|------|
| 语言 | Kotlin |
| UI | Jetpack Compose (Material3) |
| 最低版本 | Android 8.0 (API 26) |
| 图片加载 | Coil 2.5.0 |
| 网络请求 | Retrofit 2.9.0 + OkHttp |
| 本地存储 | Room 2.6.1 |
| 导航 | Navigation Compose 2.7.6 |
| 协程 | Kotlin Coroutines 1.7.3 |
| 后端 | Python Flask + 阿里云百炼 qwen3.6-plus |

## 📂 项目结构

```
CatCompanion/
├── app/
│   ├── build.gradle                    # App模块依赖配置
│   └── src/main/
│       ├── AndroidManifest.xml         # 应用清单+权限配置
│       ├── res/
│       │   ├── values/strings.xml      # 字符串资源
│       │   └── xml/
│       │       ├── network_security_config.xml  # HTTP明文访问配置
│       │       ├── backup_rules.xml
│       │       └── data_extraction_rules.xml
│       └── java/com/catcompanion/app/
│           ├── CatApplication.kt        # 全局应用类（初始化Room+Coil）
│           ├── ui/
│           │   ├── theme/               # 主题定义
│           │   │   ├── Colors.kt        # 配色方案
│           │   │   ├── Theme.kt         # Material3主题
│           │   │   └── Type.kt          # 字体排版
│           │   ├── onboarding/          # 引导页
│           │   │   └── OnboardingScreen.kt
│           │   ├── home/                # 首页
│           │   │   ├── HomeScreen.kt
│           │   │   └── MainActivity.kt
│           │   ├── chat/                # AI聊天页
│           │   │   └── ChatScreen.kt
│           │   ├── story/               # 故事模块
│           │   │   ├── StoryListScreen.kt
│           │   │   └── StoryDetailScreen.kt
│           │   └── nav/                 # 导航路由
│           │       └── NavGraph.kt
│           └── data/
│               ├── local/
│               │   ├── entity/          # Room数据实体
│               │   │   ├── CatEntity.kt
│               │   │   ├── ChatMessageEntity.kt
│               │   │   └── StoryEntity.kt
│               │   ├── dao/             # 数据访问对象
│               │   │   ├── CatDao.kt
│               │   │   ├── ChatMessageDao.kt
│               │   │   └── StoryDao.kt
│               │   └── database/
│               │       └── AppDatabase.kt
│               ├── model/               # API模型
│               │   └── ApiModels.kt
│               ├── network/             # 网络层
│               │   ├── ApiService.kt    # Retrofit接口
│               │   └── RetrofitClient.kt
│               ├── repository/
│               │   └── AppRepository.kt # 数据仓库
│               └── viewmodel/           # 视图模型
│                   ├── ChatViewModel.kt
│                   ├── HomeViewModel.kt
│                   ├── OnboardingViewModel.kt
│                   └── StoryViewModel.kt
├── build.gradle                         # 项目级构建配置
├── settings.gradle                      # 项目设置
└── gradle.properties                    # Gradle属性
```

## 🚀 快速开始

### 环境要求

- **Android Studio**: Hedgehog 2023.1.1 或更高版本
- **JDK**: 17 (Android Gradle Plugin 8.2.0 要求)
- **Gradle**: 8.2+
- **目标SDK**: 34

### 构建步骤

1. **导入项目**
   ```
   打开 Android Studio → File → Open → 选择 CatCompanion 文件夹
   ```

2. **同步 Gradle**
   ```
   Tools → Sync Project with Gradle Files
   ```

3. **配置后端地址（可选）**
   
   编辑 [RetrofitClient.kt](app/src/main/java/com/catcompanion/app/data/network/RetrofitClient.kt) 中的 ConfigManager.BASE_URL：
   ```kotlin
   // 开发阶段：Android模拟器访问本机
   const val BASE_URL = "http://10.0.2.2:5000"
   
   // 真机测试：替换为你的电脑IP地址
   const val BASE_URL = "http://192.168.1.x:5000"
   ```

4. **运行 App**
   - 连接 Android 设备或使用模拟器
   - 点击 Run 按钮（▶️）

## 🔧 配置说明

### 网络权限
- `INTERNET` - 网络请求
- `ACCESS_NETWORK_STATE` - 检查网络状态
- HTTP明文流量已配置（network_security_config.xml），允许访问 localhost/127.0.0.1/10.0.2.2:5000

### 后端接口对接
所有 API 接口在 [ApiService.kt](app/src/main/java/com/catcompanion/app/data/network/ApiService.kt) 中定义：

| 接口 | 方法 | 描述 |
|------|------|------|
| `/chat` | POST | AI聊天（参数：message, pet_type） |
| `/story/list` | GET | 故事列表（参数：pet_type） |
| `/story/detail` | GET | 故事详情（参数：id） |
| `/clear_history` | GET | 清空聊天上下文（参数：pet_type） |

### Room 数据库
数据库文件位于应用私有目录：`/data/data/com.catcompanion.app/databases/cat_companion_db`

## 📖 页面说明

1. **引导页**（6页滑动）→ 介绍猫咪 → 选择猫咪
2. **首页** → 展示猫咪形象 + 聊天/故事入口
3. **AI聊天页** → 气泡式对话 + 底部输入框
4. **故事列表页** → 10章章节列表（8-10章锁定）
5. **故事详情页** → 完整正文阅读

## 🎨 猫咪人设

| 标识 | 名称 | 性格 | 代表色 |
|------|------|------|--------|
| fubao | 福宝 | 绅士稳重、温柔贴心、暖心大哥哥 | 蓝色 #4A90D9 |
| buding | 小布丁 | 阳光活泼、热情开朗、元气弟弟 | 橙棕 #F4A460 |
| xixi | 茜茜 | 温柔细腻、善于倾听、知心姐姐 | 粉色 #E91E8C |
| paofu | 泡芙 | 傲娇俏皮、嘴硬心软、元气大小姐 | 红色 #FF6347 |

## ⚙️ 开发注意事项

- 开发阶段使用模拟数据（[AppRepository.kt](app/src/main/java/com/catcompanion/app/data/repository/AppRepository.kt)）
- 对接真实后端时需修改 `ConfigManager.BASE_URL`
- 切换猫咪时自动重置聊天记录（ChatScreen.kt）
- 故事第8-10章已实现锁逻辑（StoryListScreen.kt）
