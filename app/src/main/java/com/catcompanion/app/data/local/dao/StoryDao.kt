package com.catcompanion.app.data.local.dao

import androidx.room.*
import com.catcompanion.app.data.local.entity.StoryEntity

/**
 * 故事数据访问对象 - Room DAO
 * 管理故事章节的增删查操作，支持按猫咪ID查询列表和获取详情
 */
@Dao
interface StoryDao {

    /** 根据猫咪ID查询该猫的所有故事章节（按章节号排序） */
    @Query("SELECT * FROM stories WHERE petId = :petId ORDER BY chapterNumber ASC")
    suspend fun getStoriesByPet(petId: String): List<StoryEntity>

    /** 根据ID查询单章故事详情 */
    @Query("SELECT * FROM stories WHERE id = :id")
    suspend fun getStoryById(id: String): StoryEntity?

    /** 批量插入故事数据（初始化时用） */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllStories(stories: List<StoryEntity>)

    /** 清空所有故事数据 */
    @Query("DELETE FROM stories")
    suspend fun deleteAll()
}
