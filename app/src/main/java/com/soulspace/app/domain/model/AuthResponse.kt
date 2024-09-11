package com.soulspace.app.domain.model

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    val data: AuthItem
)

data class AuthItem(
    @SerializedName("access_token")
    val accessToken: String,
    val user: User
)

data class User(
    val id: Int,
    val email: String,
    @SerializedName("is_admin")
    val isAdmin: Boolean,
    val name: String,
    val created_at: String,
    val updated_at: String,
    val email_verified_at: Any,
)