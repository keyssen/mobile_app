package com.nodj.hardwareStore

import androidx.lifecycle.MutableLiveData
import com.nodj.hardwareStore.db.models.User

class LiveStore {
    companion object {
        val user = MutableLiveData<User?>(null)
        val searchRequest = MutableLiveData("")

        fun getUserId(): Int {
            return this.user.value?.id ?: 0
        }
    }
}
