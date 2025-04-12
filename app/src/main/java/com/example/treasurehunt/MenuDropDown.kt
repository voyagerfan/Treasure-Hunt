package com.example.treasurehunt

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.treasurehunt.ui.theme.catamaranFamily

@Composable
fun MenuDropdown() {

    var expanded by remember {  mutableStateOf(false) }
    IconButton(
        onClick = {
            expanded = true
        }
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_menu_24),
            contentDescription = "Menu Icon"
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        DropdownMenuItem (
            onClick = { /* TODO: add route */ expanded = false },
            text = {
                Text(
                    modifier = Modifier.padding(end = 50.dp),
                    text = "Login",
                    fontFamily = catamaranFamily,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.baseline_login_24),
                    contentDescription = "Login Icon"
                )
            },
        )

        DropdownMenuItem (
            onClick = { /* TODO: add route */ expanded = false },
            text = {
                Text(
                    modifier = Modifier.padding(end = 50.dp),
                    text = "Dashboard",
                    fontFamily = catamaranFamily,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.baseline_dashboard_24),
                    contentDescription = "Dashboard Icon"
                )
            },
        )

        DropdownMenuItem (
            onClick = { /* TODO: add route */ expanded = false },
            text = {
                Text(
                    modifier = Modifier.padding(end = 50.dp),
                    text = "Settings",
                    fontFamily = catamaranFamily,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.baseline_settings_24),
                    contentDescription = "Setting Icon"
                )
            },
        )

        DropdownMenuItem (
            onClick = { /* TODO: add route */ expanded = false },
            text = {
                Text(
                    modifier = Modifier.padding(end = 50.dp),
                    text = "Logout",
                    fontFamily = catamaranFamily,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.baseline_logout_24),
                    contentDescription = "Logout Icon"
                )
            },
        )
    }
}

@Composable
@Preview
fun PreviewMenu() {
    MenuDropdown()
}