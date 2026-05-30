package com.catcompanion.app.data.model

/**
 * 聊天请求体 - POST /chat 接口参数
 */
data class ChatRequest(
    val message: String,      // 用户消息内容
    val pet_type: String      // 猫咪角色标识：fubao / buding / xixi / paofu
)

/**
 * 聊天响应体 - POST /chat 接口返回
 */
data class ChatResponse(
    val reply: String         // AI 回复内容
)

/**
 * 故事列表项 - GET /story/list 接口返回
 */
data class StoryItem(
    val id: String,           // 故事ID
    val petId: String,        // 所属猫咪标识
    val chapterNumber: Int,   // 章节号
    val chapterTitle: String, // 章节标题
    val isLocked: Boolean     // 是否付费锁定
)

/**
 * 故事详情 - GET /story/detail?id=xxx 接口返回
 */
data class StoryDetail(
    val id: String,
    val petId: String,
    val chapterNumber: Int,
    val chapterTitle: String,
    val content: String       // 故事正文
)
