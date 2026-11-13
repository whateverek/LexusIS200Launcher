
package com.is200.launcher.ui.components.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.is200.launcher.ui.theme.AccentColor
import com.is200.launcher.system.status.BluetoothStatus
import com.is200.launcher.system.status.LocationStatus
import com.is200.launcher.system.status.NetworkStatus
import com.is200.launcher.system.status.SystemStatusManager
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun TopBar() {

    val context = LocalContext.current

    val statusManager = remember {
        SystemStatusManager(context)
    }

    val systemStatus by statusManager.status.collectAsState()
    val hardwareStatus by statusManager.hardwareStatus.collectAsState()

    var showHardware by remember {
        mutableStateOf(false)
    }

    val currentTime = remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        while (true) {
            val formatter = java.text.SimpleDateFormat(
                "HH:mm",
                Locale.getDefault()
            )

            currentTime.value = formatter.format(
                java.util.Date()
            )

            delay(1000)
        }
    }

    val gpsValue = when (systemStatus.location) {
        LocationStatus.DISABLED -> "WYŁ."
        LocationStatus.PERMISSION_REQUIRED -> "BRAK ZGODY"
        LocationStatus.NO_GPS_PROVIDER -> "BRAK GPS"
        LocationStatus.SEARCHING -> "SZUKANIE"
        LocationStatus.FIXED -> "AKTYWNY"
    }

    val gpsActive =
        systemStatus.location == LocationStatus.FIXED

    val networkValue = when (systemStatus.network) {
        NetworkStatus.DISCONNECTED -> "OFFLINE"
        NetworkStatus.CONNECTED_NO_INTERNET -> "BRAK INTERNETU"
        NetworkStatus.WIFI -> "WIFI"
        NetworkStatus.MOBILE -> "DANE MOB"
        NetworkStatus.ETHERNET -> "ETHERNET"
        NetworkStatus.OTHER -> "ONLINE"
    }

    val networkActive =
        systemStatus.network == NetworkStatus.WIFI ||
                systemStatus.network == NetworkStatus.MOBILE ||
                systemStatus.network == NetworkStatus.ETHERNET ||
                systemStatus.network == NetworkStatus.OTHER

    val bluetoothValue = when (systemStatus.bluetooth) {
        BluetoothStatus.PERMISSION_REQUIRED -> "BRAK ZGODY"
        BluetoothStatus.OFF -> "WYŁĄCZONY"
        BluetoothStatus.ON -> "WŁĄCZONY"
        BluetoothStatus.CONNECTED -> "POŁĄCZONY"
    }

    val bluetoothActive =
        systemStatus.bluetooth == BluetoothStatus.ON ||
                systemStatus.bluetooth == BluetoothStatus.CONNECTED

    val cpuTemperature = listOfNotNull(
        hardwareStatus.cpuLittleTemperature,
        hardwareStatus.cpuBigTemperature
    ).maxOrNull()

    val cpuValue = cpuTemperature?.let {
        "${it.toInt()}°C"
    } ?: "--"

    val gpuValue = hardwareStatus.gpuTemperature?.let {
        "${it.toInt()}°C"
    } ?: "--"

    val ramValue = if (hardwareStatus.totalRamMb > 0) {
        val usedGb = hardwareStatus.usedRamMb / 1024f
        val totalGb = hardwareStatus.totalRamMb / 1024f

        String.format(
            Locale.getDefault(),
            "%.1f/%.1f GB",
            usedGb,
            totalGb
        )
    } else {
        "--"
    }

    val cpuActive = cpuTemperature != null
    val gpuActive = hardwareStatus.gpuTemperature != null
    val ramActive = hardwareStatus.totalRamMb > 0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(78.dp)
            .clickable {
                showHardware = !showHardware
            }
            .padding(horizontal = 28.dp),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "LEXUS",
                color = Color.White,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(AccentColor)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (!showHardware) {

                StatusChip(
                    label = "GPS",
                    value = gpsValue,
                    active = gpsActive
                )

                StatusChip(
                    label = "Internet",
                    value = networkValue,
                    active = networkActive
                )

                StatusChip(
                    label = "Bluetooth",
                    value = bluetoothValue,
                    active = bluetoothActive
                )

            } else {

                StatusChip(
                    label = "CPU",
                    value = cpuValue,
                    active = cpuActive
                )

                StatusChip(
                    label = "GPU",
                    value = gpuValue,
                    active = gpuActive
                )

                StatusChip(
                    label = "RAM",
                    value = ramValue,
                    active = ramActive
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = currentTime.value,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun StatusChip(
    label: String,
    value: String,
    active: Boolean = true
) {

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(
                if (active) {
                    Color(0xFF141414)
                } else {
                    Color(0xFF101010)
                }
            )
            .padding(
                horizontal = 12.dp,
                vertical = 6.dp
            ),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(
                    if (active) {
                        AccentColor
                    } else {
                        Color.Gray
                    }
                )
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = label,
            color = Color.Gray,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = value,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
