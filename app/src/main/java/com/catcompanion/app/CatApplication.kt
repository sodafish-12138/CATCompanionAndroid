package com.catcompanion.app

import android.app.Application
import com.catcompanion.app.data.local.database.AppDatabase
import com.catcompanion.app.data.repository.AppRepository
import coil.ImageLoader
import coil.ImageLoaderFactory

/**
 * 全局应用类 - 继承 Application 实现单例化管理
 * 初始化 Room 数据库和 Coil 图片加载器
 */
class CatApplication : Application(), ImageLoaderFactory {

    // Room 数据库单例，整个应用生命周期内唯一
    val database by lazy { AppDatabase.getInstance(this) }

    // 数据仓库单例，供 ViewModel 层使用
    val repository by lazy { AppRepository() }

    /**
     * 返回 Coil 图片加载器实例
     * 配置全局图片加载策略：缓存、占位图等
     */
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .crossfade(true)       // 启用渐变动画
            .respectCacheHeaders(false)  // 不尊重服务器缓存头（开发阶段）
            .build()
    }
}
