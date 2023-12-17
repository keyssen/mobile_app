package com.nodj.hardwareStore.db.models

enum class UserRole(val value: Int) {
    UNAUTHENTICATED(-1),
    USER(0),
    ADMIN(1)
}
