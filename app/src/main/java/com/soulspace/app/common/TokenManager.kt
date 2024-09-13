package com.soulspace.app.common

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    private val sharedPreferences = context.getSharedPreferences("soulspace_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val ACCESS_TOKEN = PreferencesKey.ACCESS_TOKEN
        private const val USER_ID = PreferencesKey.USER_ID
        private const val ROOM_ID = PreferencesKey.ROOM_ID
    }

    fun saveToken(token: String) {
        sharedPreferences.edit().putString(ACCESS_TOKEN, token).apply()
    }

    fun getToken(): String {
        return sharedPreferences.getString(ACCESS_TOKEN, "") ?: ""
    }

    fun getRoomId(): String {
        return sharedPreferences.getString(ROOM_ID, "") ?: ""
    }

    fun clearToken() {
        sharedPreferences.edit().remove(ACCESS_TOKEN).apply()
    }

    fun saveAuthPrefs(token: String, userId: Int, roomId: Int) {
        sharedPreferences.edit().putString(ACCESS_TOKEN, token).apply()
        sharedPreferences.edit().putInt(USER_ID, userId).apply()
        sharedPreferences.edit().putInt(ROOM_ID, roomId).apply()
    }

    fun clearAuthPrefs() {
        sharedPreferences.edit().remove(ACCESS_TOKEN).apply()
        sharedPreferences.edit().remove(USER_ID).apply()
        sharedPreferences.edit().remove(ROOM_ID).apply()
    }
}