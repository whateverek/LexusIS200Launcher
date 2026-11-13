package com.is200.launcher.ui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.is200.launcher.ui.theme.BorderColor
import androidx.compose.material3.Text
@Composable
fun NavigationSearchBar(

    value: String,

    onValueChange: (String) -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(62.dp)
            .border(
                width = 1.dp,
                color = BorderColor.copy(alpha = 0.22f),
                shape = RoundedCornerShape(18.dp)
            )
            .background(
                Color(0xCC101010),
                RoundedCornerShape(18.dp)
            )
    ) {

        TextField(

            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.White
            ),
            value = value,

            onValueChange = onValueChange,

            placeholder = {

                androidx.compose.material3.Text(
                    text = "Search destination",
                    color = Color.Gray
                )
            },

            singleLine = true,

            colors = TextFieldDefaults.colors(

                focusedContainerColor =
                    Color.Transparent,

                unfocusedContainerColor =
                    Color.Transparent,

                disabledContainerColor =
                    Color.Transparent,

                focusedIndicatorColor =
                    Color.Transparent,

                unfocusedIndicatorColor =
                    Color.Transparent,

                cursorColor =
                    Color.White,

                focusedTextColor =
                    Color.White,

                unfocusedTextColor =
                    Color.White
            ),

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
    }
}