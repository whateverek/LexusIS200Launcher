
package com.is200.launcher.system.kiosk

import android.app.Activity
import android.app.ActivityManager
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.util.Log

object KioskManager {

    private const val TAG = "LexusKiosk"
    private const val PREFS_NAME = "kiosk_preferences"
    private const val KEY_ALLOWED_PACKAGES = "allowed_packages"

    private fun getAdminComponent(context: Context): ComponentName {
        return ComponentName(
            context,
            KioskDeviceAdminReceiver::class.java
        )
    }

    private fun getDevicePolicyManager(
        context: Context
    ): DevicePolicyManager {
        return context.getSystemService(
            Context.DEVICE_POLICY_SERVICE
        ) as DevicePolicyManager
    }

    private fun getAllowedPackages(context: Context): MutableSet<String> {
        val preferences = context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        )

        return preferences.getStringSet(
            KEY_ALLOWED_PACKAGES,
            emptySet()
        )?.toMutableSet() ?: mutableSetOf()
    }

    private fun saveAllowedPackages(
        context: Context,
        packages: Set<String>
    ) {
        context.getSharedPreferences(
            PREFS_NAME,
            Context.MODE_PRIVATE
        ).edit()
            .putStringSet(KEY_ALLOWED_PACKAGES, packages.toSet())
            .apply()
    }

    private fun applyAllowedPackages(context: Context) {
        val devicePolicyManager = getDevicePolicyManager(context)
        val adminComponent = getAdminComponent(context)

        check(devicePolicyManager.isDeviceOwnerApp(context.packageName)) {
            "Lexus Launcher nie jest Device Owner"
        }

        val packages = getAllowedPackages(context).apply {
            add(context.packageName)
        }

        devicePolicyManager.setLockTaskPackages(
            adminComponent,
            packages.toTypedArray()
        )

        Log.i(TAG, "Allowed Lock Task packages: $packages")
    }

    fun allowApplication(
        context: Context,
        packageName: String
    ): Boolean {
        val appContext = context.applicationContext

        return try {
            require(packageName.isNotBlank()) {
                "Package name cannot be empty"
            }

            val devicePolicyManager =
                getDevicePolicyManager(appContext)

            check(
                devicePolicyManager.isDeviceOwnerApp(
                    appContext.packageName
                )
            ) {
                "Lexus Launcher nie jest Device Owner"
            }

            appContext.packageManager.getApplicationInfo(
                packageName,
                0
            )

            val packages = getAllowedPackages(appContext)
            packages.add(appContext.packageName)
            packages.add(packageName)

            devicePolicyManager.setLockTaskPackages(
                getAdminComponent(appContext),
                packages.toTypedArray()
            )

            saveAllowedPackages(appContext, packages)

            Log.i(TAG, "Application allowed: $packageName")

            true

        } catch (e: Exception) {
            Log.e(
                TAG,
                "Cannot allow application: $packageName",
                e
            )
            false
        }
    }

    fun enableKiosk(activity: Activity) {
        val context = activity.applicationContext

        try {
            val devicePolicyManager =
                getDevicePolicyManager(context)

            val adminComponent = getAdminComponent(context)

            check(
                devicePolicyManager.isDeviceOwnerApp(
                    context.packageName
                )
            ) {
                "Lexus Launcher nie jest Device Owner"
            }

            check(
                devicePolicyManager.isAdminActive(adminComponent)
            ) {
                "Kiosk Device Admin nie jest aktywny"
            }

            Log.i(TAG, "Device Owner confirmed")

            applyAllowedPackages(context)

            devicePolicyManager.setLockTaskFeatures(
                adminComponent,
                DevicePolicyManager.LOCK_TASK_FEATURE_NONE
            )

            devicePolicyManager.setStatusBarDisabled(
                adminComponent,
                true
            )

            val activityManager =
                context.getSystemService(
                    Context.ACTIVITY_SERVICE
                ) as ActivityManager

            if (
                activityManager.lockTaskModeState ==
                ActivityManager.LOCK_TASK_MODE_NONE
            ) {
                Log.i(TAG, "Starting Lock Task")
                activity.startLockTask()
            } else {
                Log.i(TAG, "Lock Task already active")
            }

            Log.i(TAG, "Kiosk successfully activated")

        } catch (e: Exception) {
            Log.e(TAG, "Kiosk activation failed", e)
        }
    }

    fun disableKiosk(activity: Activity) {
        val context = activity.applicationContext

        try {
            val devicePolicyManager =
                getDevicePolicyManager(context)

            val adminComponent = getAdminComponent(context)

            if (
                devicePolicyManager.isDeviceOwnerApp(
                    context.packageName
                )
            ) {
                devicePolicyManager.setStatusBarDisabled(
                    adminComponent,
                    false
                )
            }

            activity.stopLockTask()

            Log.i(TAG, "Kiosk disabled")

        } catch (e: Exception) {
            Log.e(TAG, "Kiosk deactivation failed", e)
        }
    }
}
