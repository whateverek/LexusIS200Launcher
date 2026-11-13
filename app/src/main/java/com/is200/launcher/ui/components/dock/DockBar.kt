
package com.is200.launcher.ui.components.dock

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bluetooth
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.VolumeDown
import androidx.compose.material.icons.rounded.VolumeOff
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material.icons.rounded.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.is200.launcher.ui.theme.AccentColor
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun DockBar(
    modifier: Modifier = Modifier,
    onAppsClick: () -> Unit
) {
    val iconColor = Color(0xFFE5E5E5)
    val activeColor = AccentColor
    val dividerColor = Color(0xFF292929)

    var currentTime by remember {
        mutableStateOf(
            LocalTime.now().format(
                DateTimeFormatter.ofPattern("HH:mm")
            )
        )
    }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalTime.now().format(
                DateTimeFormatter.ofPattern("HH:mm")
            )
            delay(1000)
        }
    }

    ColumnDockBar(modifier = modifier) {

        DockGroup {
            DockIcon(Icons.Rounded.VolumeOff, iconColor)
            DockIcon(Icons.Rounded.VolumeDown, iconColor)
            DockIcon(Icons.Rounded.VolumeUp, iconColor)
        }

        DockDivider(dividerColor)

        DockGroup {
            DockIcon(Icons.Rounded.ArrowBack, iconColor)
            DockIcon(Icons.Rounded.Home, iconColor)

            DockIcon(
                icon = Icons.Rounded.Apps,
                tint = Color.White,
                backgroundColor = Color(0xFF29485C),
                size = 54,
                onClick = onAppsClick
            )

            DockIcon(Icons.Rounded.Layers, iconColor)
        }

        DockDivider(dividerColor)

        DockGroup {
            DockIcon(Icons.Rounded.Wifi, activeColor)
            DockIcon(Icons.Rounded.Bluetooth, activeColor)
        }

        DockDivider(dividerColor)

        DockGroup {
            NotificationIndicator()

            Text(
                text = currentTime,
                color = Color.White,
                fontSize = 21.sp,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(horizontal = 6.dp)
            )
        }

        DockDivider(dividerColor)

        DockGroup {
            DockIcon(Icons.Rounded.Settings, iconColor)
        }
    }
}

@Composable
private fun NotificationIndicator() {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(Color(0xFF151515), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.Notifications,
            contentDescription = "Powiadomienia",
            tint = Color(0xFFE5E5E5),
            modifier = Modifier.size(25.dp)
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .background(
                    Color(0xFF80E995),
                    RoundedCornerShape(50)
                )
                .padding(horizontal = 5.dp, vertical = 2.dp)
        ) {
            Text(
                text = "9+",
                color = Color(0xFF123D20),
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun ColumnDockBar(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(76.dp)
            .background(Color(0xFF090B0D)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFF292929))
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 22.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            content()
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFF292929))
        )
    }
}

@Composable
private fun DockGroup(
    content: @Composable () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        content()
    }
}

@Composable
private fun DockDivider(color: Color) {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(38.dp)
            .background(color)
    )
}

@Composable
private fun DockIcon(
    icon: ImageVector,
    tint: Color,
    backgroundColor: Color = Color(0xFF151515),
    size: Int = 48,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .size(size.dp)
            .background(backgroundColor, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(25.dp)
        )
    }
}
