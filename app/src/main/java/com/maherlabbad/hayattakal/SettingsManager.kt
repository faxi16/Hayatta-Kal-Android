package com.maherlabbad.hayattakal

import android.content.Context
import androidx.core.content.edit

class SettingsManager(context: Context) {
    private val prefs = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    fun getFloat(key: String, default: Float) = prefs.getFloat(key, default)
    fun getInt(key: String, default: Int) = prefs.getInt(key, default)

    fun setFloat(key: String, value: Float) {
        prefs.edit { putFloat(key, value) }
    }
    fun setInt(key: String, value: Int) {
        prefs.edit { putInt(key, value) }
    }
}