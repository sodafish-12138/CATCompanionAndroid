package com.catcompanion.app.data.network

import com.catcompanion.app.data.model.ChatRequest
import com.catcompanion.app.data.model.ChatResponse
import com.catcompanion.app.data.model.StoryDetail
import com.catcompanion.app.data.model.StoryItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * 后端 API 接口定义
 * 基础地址在 ApiClient 中配置，此处只声明相对路径
 */
interface ApiService {

    /**
     * AI 聊天接口 - POST /chat
     * 发送用户消息并获取 AI（猫咪角色）回复
     * @param message 用户输入的消息内容
     * @param pet_type 猫咪角色标识（fubao/buding/xixi/paofu）
     * @return AI 回复内容
     */
    @POST("/chat")
    suspend fun chat(
        @Body request: ChatRequest
    ): Response<ChatResponse>

    /**
     * 故事列表接口 - GET /story/list
     * @param pet_type 猫咪角色标识，用于返回该猫的故事列表
     * @return 故事章节列表
     */
    @GET("/story/list")
    suspend fun getStoryList(
        @Query("pet_type") petType: String
    ): Response<List<StoryItem>>

    /**
     * 故事详情接口 - GET /story/detail
     * @param id 故事章节ID
     * @return 故事详情（含完整正文）
     */
    @GET("/story/detail")
    suspend fun getStoryDetail(
        @Query("id") id: String
    ): Response<StoryDetail>

    /**
     * 清空聊天上下文接口 - GET /clear_history
     * @param pet_type 猫咪角色标识
     */
    @GET("/clear_history")
    suspend fun clearHistory(
        @Query("pet_type") petType: String
    ): Response<Unit>
}
