
package com.is200.launcher.system.status

import android.app.ActivityManager
import android.content.Context
import android.os.Debug
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File

class HardwareStatusMonitor(context: Context) {

    private val appContext = context.applicationContext

    private val activityManager =
        appContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

    private val thermalZones = mapOf(
        "cpuLittleTemperature" to "/sys/class/thermal/thermal_zone0/temp",
        "cpuBigTemperature" to "/sys/class/thermal/thermal_zone1/temp",
        "gpuTemperature" to "/sys/class/thermal/thermal_zone4/temp",
        "npuTemperature" to "/sys/class/thermal/thermal_zone5/temp",
        "ddrTemperature" to "/sys/class/thermal/thermal_zone6/temp"
    )

    fun observe(): Flow<HardwareStatusState> = flow {
        while (true) {
            emit(readHardwareStatus())
            delay(2000)
        }
    }.flowOn(Dispatchers.IO)

    private fun readHardwareStatus(): HardwareStatusState {
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)

        val totalRamBytes = memoryInfo.totalMem
        val availableRamBytes = memoryInfo.availMem
        val usedRamBytes = (totalRamBytes - availableRamBytes).coerceAtLeast(0)

        val totalRamMb = totalRamBytes / (1024 * 1024)
        val usedRamMb = usedRamBytes / (1024 * 1024)

        val ramUsagePercent = if (totalRamBytes > 0) {
            ((usedRamBytes * 100) / totalRamBytes).toInt()
        } else {
            0
        }

        val temperatures = thermalZones.mapValues { (_, path) ->
            readTemperature(path)
        }

        return HardwareStatusState(
            cpuLittleTemperature = temperatures["cpuLittleTemperature"],
            cpuBigTemperature = temperatures["cpuBigTemperature"],
            gpuTemperature = temperatures["gpuTemperature"],
            npuTemperature = temperatures["npuTemperature"],
            ddrTemperature = temperatures["ddrTemperature"],
            totalRamMb = totalRamMb,
            usedRamMb = usedRamMb,
            ramUsagePercent = ramUsagePercent
        )
    }

    private fun readTemperature(path: String): Float? {
        return try {
            val value = File(path).readText().trim().toLongOrNull()
                ?: return null

            value / 1000f
        } catch (_: Exception) {
            null
        }
    }
}
