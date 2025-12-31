package com.gmail.devleedaeun.todolistapp

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class TodoListApp: Application() {

    override fun onCreate() {
        super.onCreate()
        TodoDatabase.getTodoDatabase(this)
        KakaoSdk.init(this, BuildConfig.KAKAO_API_KEY)
    }
}