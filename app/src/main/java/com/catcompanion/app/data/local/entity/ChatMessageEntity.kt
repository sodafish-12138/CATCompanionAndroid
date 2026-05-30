package com.catcompanion.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 聊天消息实体 - Room数据库中保存每次聊天的气泡记录
 * 每条消息包含发送者类型、内容、时间戳，用于本地持久化聊天记录
 */
@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val roomId: String,                   // 房间ID：由选中猫咪标识决定（如"fubao_chat"）
    val message: String,                  // 消息内容
    val sender: SenderType,               // 发送者：USER 或 AI
    val timestamp: Long                   // 时间戳（毫秒）
)

/**
 * 消息发送者枚举
 */
enum class SenderType {
    USER,     // 用户发送的消息
    AI        // AI（猫咪角色）回复的消息
}
