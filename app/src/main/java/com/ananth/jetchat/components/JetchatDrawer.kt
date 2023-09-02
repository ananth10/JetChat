package com.ananth.jetchat.components


import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ananth.jetchat.R
import com.ananth.jetchat.ui.theme.JetchatTheme

@Composable
fun JetchatDrawerContent(
    onProfileClicked: (String) -> Unit,
    onChatClicked: (String) -> Unit
) {
    // Use windowInsetsTopHeight() to add a spacer which pushes the drawer content
    // below the status bar (y-axis)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        DrawerHeader()
        DividerItem()
        DrawerItemHeader(text = "Chat")
        chatItem(text = "composers", isSelected = true) {
            onChatClicked("composers")
        }
        chatItem(text = "droidcon-nyc", isSelected = false) {
            onChatClicked("droidcon-nyc")
        }
        DividerItem(modifier = Modifier.padding(horizontal = 28.dp))
        DrawerItemHeader(text = "Recent Profiles")
        ProfileItem(text = "Ali(you)", profilePic = R.drawable.ali) {
            onProfileClicked("Ali")
        }
        ProfileItem(text = "Taylor Brook", profilePic = R.drawable.someone_else) {
            onProfileClicked("Taylor Brook")
        }
    }
}

@Composable
fun DrawerHeader() {
    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
        JetchatIcon(contentDescription = null, modifier = Modifier.size(24.dp))
        Image(
            painter = painterResource(id = R.drawable.jetchat_logo),
            contentDescription = null,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun DrawerItemHeader(text: String) {
    Box(
        modifier = Modifier
            .heightIn(52.dp)
            .padding(horizontal = 28.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun chatItem(
    text: String,
    isSelected: Boolean,
    onChatItemClicked: () -> Unit
) {

    val backGround = if (isSelected) {
        Modifier.background(MaterialTheme.colorScheme.primaryContainer)
    } else {
        Modifier
    }

    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(
                CircleShape
            )
            .then(backGround)
            .clickable(onClick = onChatItemClicked),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val iconTint = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_jetchat),
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
        )
        Text(
            text = text, style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
fun ProfileItem(text: String, @DrawableRes profilePic: Int?, onProfileClicked: () -> Unit) {
    Row(
        modifier = Modifier
            .height(56.dp)
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .clip(CircleShape)
            .clickable(onClick = onProfileClicked),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val paddingModifier = Modifier
            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp)
            .size(24.dp)

        if (profilePic != null) {
            Image(
                painter = painterResource(id = profilePic),
                contentDescription = null,
                modifier = paddingModifier.then(
                    Modifier.clip(
                        CircleShape
                    )
                ),
                contentScale = ContentScale.Crop
            )
        } else {
            Spacer(modifier = paddingModifier)
        }

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 12.dp)
        )
    }
}

@Composable
fun DividerItem(modifier: Modifier = Modifier) {
    Divider(
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
}

@Preview
@Composable
fun DrawerPreview() {
    JetchatTheme {
        Surface {
            Column {
                JetchatDrawerContent(onProfileClicked = {}, onChatClicked = {})
            }
        }
    }
}

@Preview
@Composable
fun DrawerPreviewDark() {
    JetchatTheme(isDarkTheme = true) {
        Surface {
            Column {
                JetchatDrawerContent(onProfileClicked = {}, onChatClicked = {})
            }
        }
    }
}