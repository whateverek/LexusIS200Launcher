package com.is200.launcher.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.is200.launcher.MainActivity

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        if (
            intent.action ==
            Intent.ACTION_BOOT_COMPLETED
        ) {

            val launchIntent = Intent(
                context,
                MainActivity::class.java
            )

            launchIntent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
            )

            launchIntent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
            )

            context.startActivity(
                launchIntent
            )
        }
    }
}