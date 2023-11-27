package com.nodj.hardwareStore.ui

import android.app.Application
import com.nodj.hardwareStore.common.AppContainer
import com.nodj.hardwareStore.common.AppDataContainer

class ShopApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}