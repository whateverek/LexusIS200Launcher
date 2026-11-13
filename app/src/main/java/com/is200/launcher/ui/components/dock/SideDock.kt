
package com.is200.launcher.ui.components.dock

import android.content.ComponentName
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.is200.launcher.model.LauncherApp
import com.is200.launcher.system.kiosk.KioskManager
import com.is200.launcher.ui.theme.AccentColor
import com.is200.launcher.ui.theme.BorderColor
import com.is200.launcher.ui.theme.SurfaceColor
import com.is200.launcher.ui.theme.SurfaceLight
import com.is200.launcher.utils.drawableToBitmap

@Composable
fun SideDock(
    pinnedApps: List<LauncherApp>,
    onAppsClick: () -> Unit,
    onRemoveApp: (LauncherApp) -> Unit
) {
    val context = LocalContext.current

    val isEditMode = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .width(92.dp)
            .fillMaxHeight()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(30.dp),
                ambientColor = BorderColor.copy(alpha = 0.22f),
                spotColor = BorderColor.copy(alpha = 0.18f)
            )
            .clip(RoundedCornerShape(30.dp))
            .border(
                width = 1.4.dp,
                color = BorderColor.copy(alpha = 0.48f),
                shape = RoundedCornerShape(30.dp)
            )
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF101317),
                        Color(0xFF0A0C0F)
                    )
                )
            )
            .drawBehind {
                drawRoundRect(
                    color = BorderColor.copy(alpha = 0.16f),
                    size = size,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                        30.dp.toPx(),
                        30.dp.toPx()
                    ),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 10f
                    )
                )
            }
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF161A1F),
                        Color(0xFF0E1115)
                    )
                )
            )
            .padding(vertical = 16.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        DockAppsButton(
            onClick = onAppsClick
        )

        pinnedApps.forEach { app ->
            val bitmap = remember(app.icon) {
                drawableToBitmap(app.icon)
            }

            Box(
                modifier = Modifier.size(58.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF1A1A1A),
                                    Color(0xFF101010)
                                )
                            )
                        )
                        .clickable {
                            if (!isEditMode.value) {
                                try {
                                    val allowed = KioskManager.allowApplication(
                                        context = context,
                                        packageName = app.packageName
                                    )

                                    if (!allowed) {
                                        throw IllegalStateException(
                                            "Nie udało się dopuścić aplikacji do trybu kiosku"
                                        )
                                    }

                                    val intent = Intent().apply {
                                        component = ComponentName(
                                            app.packageName,
                                            app.launchActivity
                                        )
                                        action = Intent.ACTION_MAIN
                                        addFlags(
                                            Intent.FLAG_ACTIVITY_NEW_TASK
                                        )
                                    }

                                    context.startActivity(intent)

                                } catch (exception: Exception) {
                                    Toast.makeText(
                                        context,
                                        "Nie można uruchomić ${app.appName}: ${exception.localizedMessage}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        },

                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = app.appName,
                        modifier = Modifier.size(30.dp)
                    )
                }

                if (isEditMode.value) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = 8.dp, y = (-8).dp)
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Color.Red)
                            .clickable {
                                onRemoveApp(app)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "−",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        if (isEditMode.value && pinnedApps.size < 7) {
            repeat(7 - pinnedApps.size) {
                Box(
                    modifier = Modifier
                        .size(58.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0x22FFFFFF))
                        .clickable {
                            onAppsClick()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        color = Color.Gray,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.weight(1f)
        )

        DockEditButton(
            isEditMode = isEditMode.value,
            onClick = {
                isEditMode.value = !isEditMode.value
            }
        )
    }
}

@Composable
fun DockAppsButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(62.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        AccentColor.copy(alpha = 0.35f),
                        AccentColor.copy(alpha = 0.15f)
                    )
                )
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "≡",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun DockEditButton(
    isEditMode: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(58.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isEditMode) {
                    AccentColor.copy(alpha = 0.35f)
                } else {
                    Color(0xFF151515)
                }
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "✎",
            color = Color.White,
            fontSize = 22.sp
        )
    }
}

@Composable
fun DockButton(
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundBrush = if (selected) {
        Brush.verticalGradient(
            listOf(
                AccentColor.copy(alpha = 0.35f),
                AccentColor.copy(alpha = 0.12f)
            )
        )
    } else {
        Brush.verticalGradient(
            listOf(
                SurfaceLight,
                SurfaceColor
            )
        )
    }

    Box(
        modifier = Modifier
            .size(68.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundBrush)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(30.dp)
        )
    }
}
