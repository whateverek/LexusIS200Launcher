
package com.is200.launcher.system.status

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class SystemStatusManager(context: Context) {

    private val appContext = context.applicationContext

    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main.immediate
    )

    private val locationMonitor = LocationStatusMonitor(appContext)
    private val networkMonitor = NetworkStatusMonitor(appContext)
    private val bluetoothMonitor = BluetoothStatusMonitor(appContext)
    private val hardwareMonitor = HardwareStatusMonitor(appContext)

    val status: StateFlow<SystemStatusState> =
        combine(
            locationMonitor.observe(),
            networkMonitor.observe(),
            bluetoothMonitor.observe()
        ) { location, network, bluetooth ->

            SystemStatusState(
                location = location,
                network = network,
                bluetooth = bluetooth
            )

        }.stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SystemStatusState()
        )

    val hardwareStatus: StateFlow<HardwareStatusState> =
        hardwareMonitor.observe().stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HardwareStatusState()
        )
}
