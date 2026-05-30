package com.catcompanion.app.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.catcompanion.app.data.local.dao.CatDao
import com.catcompanion.app.data.local.dao.ChatMessageDao
import com.catcompanion.app.data.local.dao.StoryDao
import com.catcompanion.app.data.local.entity.CatEntity
import com.catcompanion.app.data.local.entity.ChatMessageEntity
import com.catcompanion.app.data.local.entity.StoryEntity

/**
 * App 主数据库 - Room Database 单例
 * 包含猫咪表、聊天消息表、故事表三个数据表
 * 使用 @Database 注解定义所有实体类和版本
 */
@Database(
    entities = [CatEntity::class, ChatMessageEntity::class, StoryEntity::class],
    version = 1,                  // 数据库版本号，升级时需迁移
    exportSchema = true           // 导出 schema 用于版本管理
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun catDao(): CatDao
    abstract fun chatMessageDao(): ChatMessageDao
    abstract fun storyDao(): StoryDao

    companion object {
        // 数据库名称，Room 会在应用私有目录下创建此 SQLite 数据库文件
        const val DATABASE_NAME = "cat_companion_db"

        /** 通过双重检查锁定实现线程安全的单例模式 */
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    // 开发阶段允许在主线程查询（实际生产应避免）
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
