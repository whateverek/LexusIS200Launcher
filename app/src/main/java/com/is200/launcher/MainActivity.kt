
package com.is200.launcher

import android.app.admin.DevicePolicyManager
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.is200.launcher.system.kiosk.KioskManager
import com.is200.launcher.ui.screens.home.LexusLauncherRoot

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "LexusMainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )

        enableImmersiveMode()

        setContent {
            LexusLauncherRoot()
        }
    }

    override fun onResume() {
        super.onResume()

        Log.i(TAG, "ON RESUME EXECUTED")

        enableImmersiveMode()

        window.decorView.post {
            try {
                val dpm = getSystemService(
                    DevicePolicyManager::class.java
                )

                if (dpm.isDeviceOwnerApp(packageName)) {

                    Log.i(TAG, "Device Owner confirmed")
                    Log.i(TAG, "Starting Kiosk Manager")

                    KioskManager.enableKiosk(this)

                    // Ponowne ukrycie pasków po uruchomieniu kiosku
                    enableImmersiveMode()

                } else {
                    Log.e(TAG, "Application is not Device Owner")
                }

            } catch (e: Exception) {
                Log.e(TAG, "Kiosk initialization error", e)
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (hasFocus) {
            enableImmersiveMode()
        }
    }

    private fun enableImmersiveMode() {

        WindowCompat.setDecorFitsSystemWindows(
            window,
            false
        )

        val controller = WindowInsetsControllerCompat(
            window,
            window.decorView
        )

        controller.hide(
            WindowInsetsCompat.Type.systemBars()
        )


    }
}
