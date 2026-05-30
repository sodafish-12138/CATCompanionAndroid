package com.catcompanion.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 猫咪角色实体 - Room数据库中的猫咪表
 * 存储4只猫咪角色的基本信息，用于引导页选择和首页展示
 */
@Entity(tableName = "cats")
data class CatEntity(
    @PrimaryKey val id: String,           // 猫咪标识：fubao / buding / xixi / paofu
    val displayName: String,              // 显示名称：福宝 / 小布丁 / 茜茜 / 泡芙
    val personality: String,              // 性格描述：绅士稳重的大哥哥 等
    val avatarUrl: String,                // 头像URL（网络地址或占位符）
    val colorHex: Long                    // 代表色（long类型兼容Room）
)
