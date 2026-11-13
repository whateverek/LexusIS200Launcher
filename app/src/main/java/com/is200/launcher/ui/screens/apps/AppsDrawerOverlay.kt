
package com.is200.launcher.ui.screens.apps

import android.content.ComponentName
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.is200.launcher.data.providers.AppsProvider
import com.is200.launcher.model.LauncherApp
import com.is200.launcher.system.kiosk.KioskManager
import com.is200.launcher.ui.theme.AccentColor
import com.is200.launcher.utils.drawableToBitmap

@Composable
fun AppsDrawerOverlay(
    onClose: () -> Unit,
    onPinApp: (LauncherApp) -> Unit
) {
    val context = LocalContext.current

    val apps = remember {
        AppsProvider.getInstalledApps(context)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xEE050505))
            .clickable {
                onClose()
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            AccentColor.copy(alpha = 0.03f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 140.dp,
                    top = 90.dp,
                    end = 60.dp,
                    bottom = 40.dp
                )
        ) {
            Text(
                text = "APPLICATIONS",
                color = Color.White,
                fontSize = 42.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(40.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(34.dp),
                horizontalArrangement = Arrangement.spacedBy(34.dp)
            ) {
                items(
                    items = apps,
                    key = { app -> app.packageName }
                ) { app ->
                    AppDrawerItem(
                        app = app,
                        onClick = {
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

                                val intent = Intent(
                                    Intent.ACTION_MAIN
                                ).apply {
                                    component = ComponentName(
                                        app.packageName,
                                        app.launchActivity
                                    )
                                    addCategory(Intent.CATEGORY_LAUNCHER)
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                }

                                context.startActivity(intent)
                                onClose()

                            } catch (exception: Exception) {
                                Toast.makeText(
                                    context,
                                    "Nie można uruchomić ${app.appName}: ${exception.localizedMessage}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        },
                        onLongClick = {
                            onPinApp(app)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AppDrawerItem(
    app: LauncherApp,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val bitmap = remember(app.icon) {
        drawableToBitmap(app.icon)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(124.dp)
                .clip(RoundedCornerShape(36.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF1A1A1A),
                            Color(0xFF0F0F0F)
                        )
                    )
                )
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = app.appName,
                modifier = Modifier.size(56.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = app.appName,
            color = Color.White,
            fontSize = 18.sp,
            maxLines = 1
        )
    }
}
