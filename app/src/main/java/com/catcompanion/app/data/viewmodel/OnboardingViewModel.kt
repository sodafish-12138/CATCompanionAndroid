package com.catcompanion.app.data.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.catcompanion.app.data.local.entity.CatEntity

/**
 * 引导页 ViewModel - 管理猫咪选择状态
 */
class OnboardingViewModel : ViewModel() {

    /** 已选择的猫咪（初始为null，表示尚未选择） */
    var selectedCat: CatEntity? by mutableStateOf(null)
        private set

    /** 所有可选择的猫咪列表 */
    val cats by lazy { AppRepository().getCats() }

    /** 选中某只猫咪 */
    fun selectCat(cat: CatEntity) {
        selectedCat = cat
    }

    /** 重置选择状态（回到初始状态） */
    fun resetSelection() {
        selectedCat = null
    }
}
