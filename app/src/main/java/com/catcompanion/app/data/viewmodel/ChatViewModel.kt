package com.catcompanion.app.data.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.catcompanion.app.data.local.entity.CatEntity
import com.catcompanion.app.data.local.entity.ChatMessageEntity
import com.catcompanion.app.data.local.entity.SenderType
import com.catcompanion.app.data.repository.AppRepository

/**
 * 聊天页 ViewModel - 管理聊天记录和AI回复逻辑
 * 切换猫咪角色时自动清空历史上下文，调用后端 /clear_history 接口
 */
class ChatViewModel : ViewModel() {

    /** 当前选中的猫咪 */
    var selectedCat: CatEntity? by mutableStateOf(null)
        private set

    /** 聊天消息列表（按时间顺序排列） */
    var messages: MutableList<ChatMessageEntity> by mutableStateOf(mutableListOf())
        private set

    /** 是否正在加载中（AI 回复中） */
    var isLoading by mutableStateOf(false)
        private set

    /** 初始化：加载指定猫咪的聊天记录 */
    fun initChat(cat: CatEntity) {
        // 如果已有旧数据则清空，实现切换猫咪时重置会话的效果
        messages.clear()
        selectedCat = cat

        // TODO: 从 Room 数据库加载该房间的历史记录
        // loadMessagesFromRoom(cat.id)

        // 发送一条欢迎消息作为开场白
        val welcomeMessage = when (cat.id) {
            "fubao" -> "你好呀~我是福宝，很高兴认识你。有什么想和我聊的，尽管说吧 😊"
            "buding" -> "嗨嗨！我是小布丁！快来和我聊天吧~超有趣的！🎉"
            "xixi" -> "你好呀，我是茜茜。无论开心还是难过，我都在这里倾听哦 💕"
            "paofu" -> "哼...你终于来了。本小姐等你好久了。（其实才等了五分钟啦）😤"
            else -> "你好呀！我是你的猫咪伙伴~快和我聊天吧！"
        }
        messages.add(
            ChatMessageEntity(
                roomId = "${cat.id}_chat",
                message = welcomeMessage,
                sender = SenderType.AI,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    /**
     * 发送用户消息并获取AI回复
     * @param userMessage 用户输入的消息内容
     */
    fun sendMessage(userMessage: String) {
        if (userMessage.isBlank() || selectedCat == null) return

        isLoading = true

        // 1. 添加用户消息到本地
        val userMsg = ChatMessageEntity(
            roomId = "${selectedCat!!.id}_chat",
            message = userMessage.trim(),
            sender = SenderType.USER,
            timestamp = System.currentTimeMillis()
        )
        messages.add(userMsg)

        // 2. 模拟异步网络请求获取AI回复（开发阶段使用模拟数据）
        // TODO: 生产环境替换为真实 API 调用
        val reply = AppRepository().generateAiReply(userMessage.trim(), selectedCat!!.id)

        // 3. 添加AI回复到本地
        val aiMsg = ChatMessageEntity(
            roomId = "${selectedCat!!.id}_chat",
            message = reply,
            sender = SenderType.AI,
            timestamp = System.currentTimeMillis()
        )
        messages.add(aiMsg)

        isLoading = false

        // TODO: 将聊天记录持久化到 Room
        // saveMessagesToRoom(messages)
    }

    /**
     * 切换猫咪角色时清空聊天记录
     * 同时调用后端 /clear_history 接口
     */
    fun clearHistory() {
        messages.clear()
        // TODO: 调用后端清除接口
        // clearHistoryFromBackend(selectedCat?.id ?: "fubao")
    }

    /**
     * 滚动到最新消息（供UI层调用）
     */
    fun scrollToLatest() {
        // 此处可触发UI滚动行为
    }
}
