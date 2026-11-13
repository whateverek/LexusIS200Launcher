
package com.is200.launcher.system.status

/**
 * Aktualny stan wszystkich monitorowanych usług systemowych.
 */
data class SystemStatusState(
    val location: LocationStatus = LocationStatus.DISABLED,
    val network: NetworkStatus = NetworkStatus.DISCONNECTED,
    val bluetooth: BluetoothStatus = BluetoothStatus.OFF
)

/**
 * Stan lokalizacji i odbiornika GPS.
 */
enum class LocationStatus {
    DISABLED,
    PERMISSION_REQUIRED,
    NO_GPS_PROVIDER,
    SEARCHING,
    FIXED
}

/**
 * Stan połączenia sieciowego.
 */
enum class NetworkStatus {
    DISCONNECTED,
    CONNECTED_NO_INTERNET,
    WIFI,
    MOBILE,
    ETHERNET,
    OTHER
}

/**
 * Stan adaptera Bluetooth.
 */
enum class BluetoothStatus {
    PERMISSION_REQUIRED,
    OFF,
    ON,
    CONNECTED
}
