
package com.is200.launcher.system.status

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class BluetoothStatusMonitor(context: Context) {

    private val appContext = context.applicationContext

    private val bluetoothManager =
        appContext.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager

    private val bluetoothAdapter: BluetoothAdapter?
        get() = bluetoothManager?.adapter

    fun observe(): Flow<BluetoothStatus> = callbackFlow {

        fun readStatus(): BluetoothStatus {
            val adapter = bluetoothAdapter
                ?: return BluetoothStatus.OFF

            if (!adapter.isEnabled) {
                return BluetoothStatus.OFF
            }

            val connectedProfiles = listOf(
                BluetoothProfile.HEADSET,
                BluetoothProfile.A2DP,
                BluetoothProfile.GATT
            )

            val hasConnectedDevice = connectedProfiles.any { profile ->
                try {
                    adapter.getProfileConnectionState(profile) ==
                            BluetoothProfile.STATE_CONNECTED
                } catch (_: SecurityException) {
                    false
                }
            }

            return if (hasConnectedDevice) {
                BluetoothStatus.CONNECTED
            } else {
                BluetoothStatus.ON
            }
        }

        trySend(readStatus())

        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                when (intent?.action) {
                    BluetoothAdapter.ACTION_STATE_CHANGED,
                    BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED,
                    BluetoothDeviceAction.ACTION_ACL_CONNECTED,
                    BluetoothDeviceAction.ACTION_ACL_DISCONNECTED -> {
                        trySend(readStatus())
                    }
                }
            }
        }

        val filter = IntentFilter().apply {
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
            addAction(BluetoothDeviceAction.ACTION_ACL_CONNECTED)
            addAction(BluetoothDeviceAction.ACTION_ACL_DISCONNECTED)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            appContext.registerReceiver(
                receiver,
                filter,
                Context.RECEIVER_NOT_EXPORTED
            )
        } else {
            @Suppress("DEPRECATION")
            appContext.registerReceiver(receiver, filter)
        }

        awaitClose {
            appContext.unregisterReceiver(receiver)
        }
    }

    private object BluetoothDeviceAction {
        const val ACTION_ACL_CONNECTED =
            "android.bluetooth.device.action.ACL_CONNECTED"

        const val ACTION_ACL_DISCONNECTED =
            "android.bluetooth.device.action.ACL_DISCONNECTED"
    }
}
