package com.catcompanion.app.data.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.catcompanion.app.data.local.entity.CatEntity

/**
 * 首页 ViewModel - 管理已选猫咪和付费状态
 */
class HomeViewModel : ViewModel() {

    /** 当前选中的猫咪（从 Room 或 SharedPreferences 读取） */
    var selectedCat: CatEntity? by mutableStateOf(null)
        private set

    /** 付费解锁状态：key为故事ID，value为true表示已解锁 */
    var unlockedStories: MutableSet<String> by mutableStateOf(mutableSetOf())
        private set

    /** 初始化：加载本地保存的选中猫咪信息 */
    fun initSelectedCat() {
        // TODO: 从 Room 读取上次选择的猫咪
        // 开发阶段默认选择福宝
        selectedCat = AppRepository().getCatById("fubao")
    }

    /** 切换选中的猫咪 */
    fun setSelectedCat(cat: CatEntity) {
        selectedCat = cat
        // TODO: 将选择写入 Room
    }

    /** 解锁某个付费故事章节 */
    fun unlockStory(storyId: String) {
        unlockedStories.add(storyId)
        // TODO: 将解锁状态写入 Room
    }

    /** 检查故事是否已解锁 */
    fun isStoryUnlocked(storyId: String): Boolean {
        return unlockedStories.contains(storyId) || !storyId.startsWith("ch")
    }
}
