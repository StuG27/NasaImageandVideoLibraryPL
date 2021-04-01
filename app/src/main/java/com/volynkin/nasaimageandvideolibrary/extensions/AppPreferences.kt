package com.volynkin.nasaimageandvideolibrary.extensions

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.content.edit

object AppPreferences {
    private var sharedPreferences: SharedPreferences? = null

    fun setup(context: Context) {
        sharedPreferences = context.getSharedPreferences("NASA", MODE_PRIVATE)
    }

    var json: String?
        get() = Key.JSON.getString()
        set(value) = Key.JSON.setString(value)

    private enum class Key {
        JSON;

        fun getString(): String? = if (sharedPreferences!!
                .contains(name)
        ) sharedPreferences!!
            .getString(name, "") else null

        fun setString(value: String?) = value?.let {
            sharedPreferences!!.edit { putString(name, value) }
        } ?: remove()

        fun remove() = sharedPreferences!!.edit { remove(name) }
    }
}