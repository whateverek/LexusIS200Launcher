
package com.is200.launcher.system.apps

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.is200.launcher.model.LauncherApp
import com.is200.launcher.system.kiosk.KioskManager

object AppLauncher {

    private const val TAG = "LexusAppLauncher"

    fun launch(context: Context, app: LauncherApp): Boolean {
        return try {
            val allowed = KioskManager.allowApplication(
                context = context,
                packageName = app.packageName
            )

            if (!allowed) {
                throw IllegalStateException(
                    "Nie udało się dopuścić aplikacji do trybu kiosku"
                )
            }

            val packageManager = context.packageManager

            val launchIntent =
                packageManager.getLaunchIntentForPackage(app.packageName)
                    ?: Intent(Intent.ACTION_MAIN).apply {
                        addCategory(Intent.CATEGORY_LAUNCHER)
                        component = ComponentName(
                            app.packageName,
                            app.launchActivity
                        )
                    }

            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            context.startActivity(launchIntent)

            Log.i(TAG, "Uruchomiono: ${app.packageName}")
            true

        } catch (exception: Exception) {
            Log.e(
                TAG,
                "Nie udało się uruchomić ${app.packageName}",
                exception
            )

            Toast.makeText(
                context,
                "Nie udało się uruchomić: ${app.appName}",
                Toast.LENGTH_LONG
            ).show()

            false
        }
    }
}
