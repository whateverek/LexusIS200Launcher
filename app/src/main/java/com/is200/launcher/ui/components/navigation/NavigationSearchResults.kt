package com.is200.launcher.ui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.is200.launcher.data.network.NominatimLocation
import com.is200.launcher.ui.theme.BorderColor

@Composable
fun NavigationSearchResults(

    results: List<NominatimLocation>,

    onLocationClick: (NominatimLocation) -> Unit
) {

    LazyColumn(

        verticalArrangement =
            Arrangement.spacedBy(10.dp)

    ) {

        items(results) { location ->

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xE6101010),
                        RoundedCornerShape(18.dp)
                    )
                    .clickable {

                        onLocationClick(location)
                    }
                    .padding(18.dp)
            ) {

                Text(
                    text = location.display_name,

                    color = Color.White,

                    fontSize = 15.sp,

                    maxLines = 2,

                    overflow =
                        TextOverflow.Ellipsis
                )

                Text(
                    text =
                        "${location.lat}, ${location.lon}",

                    color = BorderColor,

                    fontSize = 12.sp
                )
            }
        }
    }
}