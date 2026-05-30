package com.catcompanion.app.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Retrofit 客户端封装 - 统一管理网络请求配置
 * 开发阶段优先使用模拟数据，此处接口可切换为真实后端地址
 */

/**
 * 后端服务器基础地址（可配置）
 * 开发阶段：替换为你的本机IP，如 "http://192.168.1.100:5000"
 * Android 模拟器访问本机请使用: http://10.0.2.2:5000
 */
object ConfigManager {
    // TODO: 将 BASE_URL 替换为你实际运行的后端地址
    const val BASE_URL = "http://10.0.2.2:5000"
}

object RetrofitClient {

    private const val CONNECT_TIMEOUT = 30L  // 连接超时：30秒
    private const val READ_TIMEOUT = 30L     // 读取超时：30秒
    private const val WRITE_TIMEOUT = 30L    // 写入超时：30秒

    /**
     * 创建 OkHttp 客户端
     * 添加日志拦截器方便调试网络请求
     */
    private fun createOkHttpClient(): OkHttpClient {
        // HTTP 日志拦截器：DEBUG级别打印请求和响应信息
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    /**
     * 创建 Retrofit 实例
     * 使用 Gson 进行 JSON 序列化/反序列化
     */
    private val okHttpClient by lazy { createOkHttpClient() }

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(ConfigManager.BASE_URL)   // 设置基础地址
            .client(okHttpClient)               // 注入自定义 OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())  // 添加 Gson 转换器
            .build()
            .create(ApiService::class.java)     // 生成 API 接口的实现类
    }
}
