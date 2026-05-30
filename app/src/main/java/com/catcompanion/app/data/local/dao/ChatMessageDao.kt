package com.catcompanion.app.data.local.dao

import androidx.room.*
import com.catcompanion.app.data.local.entity.ChatMessageEntity

/**
 * 聊天消息数据访问对象 - Room DAO
 * 管理聊天记录CRUD，按房间（猫咪）查询、插入和清空记录
 */
@Dao
interface ChatMessageDao {

    /** 根据房间ID查询所有聊天消息（按时间排序） */
    @Query("SELECT * FROM chat_messages WHERE roomId = :roomId ORDER BY timestamp ASC")
    suspend fun getMessagesByRoom(roomId: String): List<ChatMessageEntity>

    /** 插入单条聊天消息 */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    /** 批量插入聊天消息 */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(messages: List<ChatMessageEntity>)

    /** 清空指定房间的聊天记录（切换猫咪时调用） */
    @Query("DELETE FROM chat_messages WHERE roomId = :roomId")
    suspend fun clearMessagesByRoom(roomId: String)

    /** 清空所有聊天记录 */
    @Query("DELETE FROM chat_messages")
    suspend fun deleteAll()
}
