package com.catcompanion.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 故事章节实体 - Room数据库中保存的故事章节数据
 * 每只猫咪有10章故事，第一人称叙事，第8-10章付费锁定
 */
@Entity(tableName = "stories")
data class StoryEntity(
    @PrimaryKey val id: String,           // 故事唯一ID：格式为 "{pet}_ch{chapter}"
    val petId: String,                    // 所属猫咪标识（fubao/buding/xixi/paofu）
    val chapterNumber: Int,               // 章节号：1-10
    val chapterTitle: String,             // 章节标题
    val content: String,                  // 故事正文内容
    val isLocked: Boolean                 // 是否付费锁定（8-10章为true）
)
