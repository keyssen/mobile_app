package com.nodj.hardwareStore.api.model

import com.nodj.hardwareStore.db.models.User
import com.nodj.hardwareStore.db.models.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class UserRemote(
    val id: Int = 0,
    val name: String = "",
    val role: UserRole = UserRole.UNAUTHENTICATED,
    val password: String = ""
)

fun UserRemote.toUser(): User = User(
    id,
    name,
    role,
    password
)

fun User.toUserRemote(): UserRemote = UserRemote(
    id,
    name,
    role,
    password
)