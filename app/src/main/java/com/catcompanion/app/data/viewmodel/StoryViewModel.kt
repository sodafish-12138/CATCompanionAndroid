package com.catcompanion.app.data.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.catcompanion.app.data.local.entity.StoryEntity
import com.catcompanion.app.data.repository.AppRepository

/**
 * 故事页 ViewModel - 管理故事列表和详情加载
 */
class StoryViewModel : ViewModel() {

    /** 当前猫咪ID */
    var currentPetId: String by mutableStateOf("fubao")
        private set

    /** 故事章节列表 */
    var stories: List<StoryEntity> by mutableStateOf(emptyList())
        private set

    /** 当前正在查看的故事详情（null表示未打开详情页） */
    var currentStory: StoryEntity? by mutableStateOf(null)
        private set

    /** 是否正在加载中 */
    var isLoading by mutableStateOf(false)
        private set

    /**
     * 初始化：加载指定猫咪的故事列表
     */
    fun loadStories(petId: String) {
        currentPetId = petId
        isLoading = true

        // TODO: 生产环境替换为真实 API 调用
        // fetchStoriesFromApi(petId)

        // 开发阶段使用本地模拟数据
        stories = AppRepository().getStoriesByPet(petId)
        isLoading = false
    }

    /**
     * 打开某章故事的详情页
     */
    fun openStoryDetail(story: StoryEntity) {
        // 如果故事被锁定且未解锁，不做任何操作
        if (story.isLocked && !isUnlocked(story.id)) return

        currentStory = story
    }

    /**
     * 关闭详情页返回列表
     */
    fun closeStoryDetail() {
        currentStory = null
    }

    /**
     * 检查付费故事是否已解锁
     */
    private fun isUnlocked(storyId: String): Boolean {
        // TODO: 从 Room 查询付费解锁状态
        return false
    }
}
