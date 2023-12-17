package com.nodj.hardwareStore.ui.page.profile

import android.content.Context
import com.nodj.hardwareStore.common.MyViewModel
import com.nodj.hardwareStore.db.datastore.PreferencesStore
import com.nodj.hardwareStore.db.repository.repositoryInterface.UserRepository

class UserViewModel(
    private val userRepository: UserRepository,

    ) : MyViewModel() {

    suspend fun Exit(context: Context) {
        val store = PreferencesStore(context)
        store.setUsername("")
        store.setUserrole("")
    }
}