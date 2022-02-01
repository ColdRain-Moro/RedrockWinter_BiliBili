package kim.bifrost.rain.bilibili.model

import kim.bifrost.rain.bilibili.App

/**
 * kim.bifrost.rain.bilibili.model.UserManager
 * BiliBili
 *
 * @author 寒雨
 * @since 2022/1/30 19:29
 **/
object UserManager {
    // token有效即为登录成功
    val isLogged: Boolean
        get() = App.token_expires_date > System.currentTimeMillis()

    /**
     * 登出
     */
    fun logout() {
        App.setToken(null, null, 0)
    }
}