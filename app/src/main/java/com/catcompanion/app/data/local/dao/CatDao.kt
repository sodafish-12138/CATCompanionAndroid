package com.catcompanion.app.data.local.dao

import androidx.room.*
import com.catcompanion.app.data.local.entity.CatEntity

/**
 * 猫咪数据访问对象 - Room DAO
 * 管理猫咪数据的增删查操作，引导页读取4只猫咪角色信息
 */
@Dao
interface CatDao {

    /** 查询所有猫咪角色（返回全部4只） */
    @Query("SELECT * FROM cats ORDER BY id ASC")
    suspend fun getAllCats(): List<CatEntity>

    /** 根据ID查询单只猫咪 */
    @Query("SELECT * FROM cats WHERE id = :id")
    suspend fun getCatById(id: String): CatEntity?

    /** 插入一只猫咪（初始化时使用） */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCat(cat: CatEntity)

    /** 批量插入猫咪数据（用于应用首次启动初始化） */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCats(cats: List<CatEntity>)

    /** 清空所有猫咪数据 */
    @Query("DELETE FROM cats")
    suspend fun deleteAll()
}
