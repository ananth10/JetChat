package com.ananth.jetchat.profile


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ananth.jetchat.FunctionalityNotAvailablePopup
import com.ananth.jetchat.R
import com.ananth.jetchat.components.AnimatingFabContent
import com.ananth.jetchat.components.baseLineHeight

@Composable
fun ProfileScreen(
    userData: ProfileScreenState,
    nestedScrollInteropConnection: NestedScrollConnection = rememberNestedScrollInteropConnection()

) {

    var functionalityNotAvailablePopupShown by remember { mutableStateOf(false) }
    if (functionalityNotAvailablePopupShown) {
        FunctionalityNotAvailablePopup {
            functionalityNotAvailablePopupShown = false
        }
    }
    var scrollState = rememberScrollState()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollInteropConnection)
            .systemBarsPadding()
    ) {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {

                ProfileHeader(
                    scrollState = scrollState,
                    data = userData,
                    containerHeight = this@BoxWithConstraints.maxHeight
                )
                UserInfoFields(
                    userData = userData,
                    containerHeight = this@BoxWithConstraints.maxHeight
                )

            }
            val fabExtended by remember {
                derivedStateOf { scrollState.value == 0 }
            }
            ProfileFab(
                extended = fabExtended,
                userIsMe = userData.isMe(),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(y = ((-100).dp)),   // Offsets the FAB to compensate for CoordinatorLayout collapsing behaviour
                onFabClicked = { functionalityNotAvailablePopupShown = true }
            )
        }

    }
}

@Composable
fun UserInfoFields(userData: ProfileScreenState, containerHeight: Dp) {
    Column {
        Spacer(modifier = Modifier.height(8.dp))
        NameAndPosition(userData)
        ProfileProperty(
            label = stringResource(id = R.string.display_name),
            value = userData.displayName
        )
        ProfileProperty(label = stringResource(id = R.string.status), value = userData.status)
        ProfileProperty(
            label = stringResource(id = R.string.twitter),
            value = userData.twitter,
            isLink = true
        )

        userData.timeZone?.let {
            ProfileProperty(
                label = stringResource(id = R.string.timezone),
                value = userData.timeZone
            )
        }
        // Add a spacer that always shows part (320.dp) of the fields list regardless of the device,
        // in order to always leave some content at the top.
        Spacer(modifier = Modifier.height((containerHeight - 360.dp).coerceAtLeast(0.dp)))
    }
}

@Composable
fun NameAndPosition(userData: ProfileScreenState) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Name(data = userData, modifier = Modifier.baseLineHeight(32.dp))
        Position(
            data = userData, modifier = Modifier
                .padding(bottom = 20.dp)
                .baseLineHeight(24.dp)
        )
    }
}

@Composable
fun Name(data: ProfileScreenState, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = data.name,
        style = MaterialTheme.typography.headlineSmall
    )
}

@Composable
fun Position(data: ProfileScreenState, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier,
        text = data.position,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun ProfileHeader(
    scrollState: androidx.compose.foundation.ScrollState,
    data: ProfileScreenState,
    containerHeight: Dp
) {
    val offset = (scrollState.value / 2)
    val offsetDp = with(LocalDensity.current) {
        offset.toDp()
    }

    data.photo?.let {
        Image(
            modifier = Modifier
                .heightIn(max = containerHeight / 2)
                .fillMaxWidth()
                .padding(start = 16.dp, top = offsetDp, end = 16.dp)
                .clip(CircleShape),
            painter = painterResource(id = it),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
    }
}

@Composable
fun ProfileProperty(label: String, value: String, isLink: Boolean = false) {
    Column(
        modifier = Modifier
            .padding(start = 16.dp)
            .padding(end = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        Divider()
        Text(
            text = label,
            modifier = Modifier.baseLineHeight(24.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        val style = if (isLink) {
            MaterialTheme.typography.bodyLarge.copy(MaterialTheme.colorScheme.primary)
        } else {
            MaterialTheme.typography.bodyLarge
        }
        Text(text = value, modifier = Modifier.baseLineHeight(24.dp), style = style)
    }
}

@Composable
fun ProfileError() {
    Text(text = stringResource(id = R.string.profile_error))
}

@Composable
fun ProfileFab(
    extended: Boolean,
    userIsMe: Boolean,
    modifier: Modifier = Modifier,
    onFabClicked: () -> Unit = {}
) {
    println("TEST: $extended")
    key(userIsMe) { //Prevent multiple invocation to execute during composition

        FloatingActionButton(
            onClick = onFabClicked,
            modifier = modifier
                .padding(16.dp)
                .navigationBarsPadding()
                .height(48.dp)
                .widthIn(min = 48.dp),
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ) {

            AnimatingFabContent(icon = {
                Icon(
                    imageVector = if (userIsMe) Icons.Outlined.Create else Icons.Outlined.Chat,
                    contentDescription = stringResource(id = if (userIsMe) R.string.edit_profile else R.string.message)
                )
            }, text = {
                Text(text = stringResource(id = if (userIsMe) R.string.edit_profile else R.string.message))
            },
                extended = extended
            )
        }
    }
}