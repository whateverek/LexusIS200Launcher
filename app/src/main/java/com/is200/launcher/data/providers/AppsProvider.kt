package com.is200.launcher.data.providers

import android.content.Context
import android.content.Intent
import com.is200.launcher.model.LauncherApp

object AppsProvider {

    fun getInstalledApps(
        context: Context
    ): List<LauncherApp> {

        return try {

            val packageManager =
                context.packageManager

            val apps =
                mutableListOf<LauncherApp>()

            val launcherIntent = Intent(
                Intent.ACTION_MAIN,
                null
            ).apply {

                addCategory(
                    Intent.CATEGORY_LAUNCHER
                )
            }

            val launcherApps =

                packageManager
                    .queryIntentActivities(
                        launcherIntent,
                        0
                    )

            launcherApps.forEach { resolveInfo ->

                try {

                    val activityInfo =
                        resolveInfo.activityInfo
                            ?: return@forEach

                    apps.add(

                        LauncherApp(

                            packageName =
                                activityInfo.packageName,

                            appName =
                                resolveInfo
                                    .loadLabel(
                                        packageManager
                                    )
                                    .toString(),

                            icon =
                                resolveInfo
                                    .loadIcon(
                                        packageManager
                                    ),

                            launchActivity =
                                activityInfo.name
                        )
                    )

                } catch (_: Exception) {

                }
            }

            val installedPackages =

                packageManager
                    .getInstalledPackages(0)

            installedPackages.forEach { packageInfo ->

                try {

                    val launchIntent =

                        packageManager
                            .getLaunchIntentForPackage(
                                packageInfo.packageName
                            )
                            ?: return@forEach

                    val appInfo =
                        packageInfo.applicationInfo
                            ?: return@forEach

                    val alreadyExists =

                        apps.any {

                            it.packageName ==
                                    packageInfo.packageName
                        }

                    if (!alreadyExists) {

                        apps.add(

                            LauncherApp(

                                packageName =
                                    packageInfo.packageName,

                                appName =
                                    packageManager
                                        .getApplicationLabel(
                                            appInfo
                                        )
                                        .toString(),

                                icon =
                                    packageManager
                                        .getApplicationIcon(
                                            appInfo
                                        ),

                                launchActivity =
                                    launchIntent
                                        .component
                                        ?.className
                                        ?: return@forEach
                            )
                        )
                    }

                } catch (_: Exception) {

                }
            }

            apps
                .distinctBy {
                    it.packageName
                }
                .sortedBy {
                    it.appName.lowercase()
                }

        } catch (_: Exception) {

            emptyList()
        }
    }
}