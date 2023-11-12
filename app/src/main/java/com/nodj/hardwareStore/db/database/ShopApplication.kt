package com.nodj.hardwareStore.db.database

import android.app.Application
import com.nodj.hardwareStore.db.database.AppContainer
import com.nodj.hardwareStore.db.database.AppDataContainer

class ShopApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}