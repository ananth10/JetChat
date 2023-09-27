package com.ananth.jetchat.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.ananth.jetchat.data.colleagueProfile
import com.ananth.jetchat.data.meProfile
import com.ananth.jetchat.ui.theme.JetchatTheme

@Preview(widthDp = 340, name = "340 with - Me")
@Composable
fun ProfilePreview340Me() {
    JetchatTheme {
        ProfileScreen(userData = meProfile)
    }
}

@Preview(widthDp = 480, name = "480 with - Me")
@Composable
fun ProfilePreview480Me() {
    JetchatTheme {
        ProfileScreen(userData = meProfile)
    }
}

@Preview(widthDp = 480, name = "480 with - Other")
@Composable
fun ProfilePreview480Other() {
    JetchatTheme {
        ProfileScreen(userData = colleagueProfile)
    }
}

@Preview(widthDp = 480, name = "340 with - Me - Dark")
@Composable
fun ProfilePreview340MeDark() {
    JetchatTheme(isDarkTheme = true) {
        ProfileScreen(userData = colleagueProfile)
    }
}

@Preview(widthDp = 480, name = "480 with - Me - Dark")
@Composable
fun ProfilePreview480MeDark() {
    JetchatTheme(isDarkTheme = true) {
        ProfileScreen(userData = meProfile)
    }
}

@Preview(widthDp = 480, name = "480 with - Other - Dark")
@Composable
fun ProfilePreview480OtherDark() {
    JetchatTheme(isDarkTheme = true) {
        ProfileScreen(userData = colleagueProfile)
    }
}