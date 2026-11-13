
package com.is200.launcher.system.status

data class HardwareStatusState(
    val cpuLittleTemperature: Float? = null,
    val cpuBigTemperature: Float? = null,
    val gpuTemperature: Float? = null,
    val npuTemperature: Float? = null,
    val ddrTemperature: Float? = null,
    val totalRamMb: Long = 0,
    val usedRamMb: Long = 0,
    val ramUsagePercent: Int = 0
)
