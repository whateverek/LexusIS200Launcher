package com.is200.launcher.data.storage

import android.content.Context

object PinnedAppsStorage {

    private const val PREFS_NAME =
        "launcher_prefs"

    private const val KEY_PINNED_APPS =
        "pinned_apps"

    fun savePinnedApps(
        context: Context,
        packageNames: List<String>
    ) {

        val prefs =
            context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )

        prefs.edit()
            .putString(
                KEY_PINNED_APPS,
                packageNames.joinToString("|")
            )
            .apply()
    }

    fun loadPinnedApps(
        context: Context
    ): List<String> {

        val prefs =
            context.getSharedPreferences(
                PREFS_NAME,
                Context.MODE_PRIVATE
            )

        val saved =
            prefs.getString(
                KEY_PINNED_APPS,
                ""
            ) ?: ""

        if (saved.isEmpty()) {

            return emptyList()
        }

        return saved.split("|")
    }
}