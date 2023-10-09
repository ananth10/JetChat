package com.ananth.jetchat.conversation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.ananth.jetchat.FunctionalityNotAvailablePopup
import com.ananth.jetchat.R
import com.ananth.jetchat.components.JetchatAppBar
import kotlinx.coroutines.launch

/**
 * Entry point for conversation screen
 *
 * @param uiState[ConversationUiState] that contains messages to display
 * @param navigateToProfile User action when navigation when navigation to profile is requested
 * @param modifier [Modifier] to apply to this layout node
 * @param onNavIconPressed Sends an event up when the user clicks on the menu
 * */

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationContent(
    uiState: ConversationUiState,
    navigateToProfile: (String) -> Unit,
    modifier: Modifier = Modifier,
    onNavIconPressed: () -> Unit = {}
) {

    val authorMe = stringResource(id = R.string.author_me)
    val timeNow = stringResource(id = R.string.now)

    val scrollState = rememberLazyListState()
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            ChannelNameBar(
                channelName = uiState.channelName,
                channelNumbers = uiState.channelMembers,
                onNavIconPressed = onNavIconPressed,
                scrollBehavior = scrollBehavior
            )
        },
        //exclude ime and navigation bar padding so this can be added by the UserInput Composable
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime),
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding)
        ) {
            Messages(
                messages = uiState.messages,
                navigateToProfile = navigateToProfile,
                scrollState = scrollState,
                modifier = Modifier.weight(1f)
            )
            UserInput(
                onMessageSent = { content ->
                    uiState.addMessages(Message(authorMe, content, timeNow))
                },
                resetScroll = {
                    scope.launch {
                        scrollState.scrollToItem(0)
                    }
                },
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
            )
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelNameBar(
    channelName: String,
    channelNumbers: Int,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavIconPressed: () -> Unit
) {

    var functionalityNotAvailablePopupShown by remember {
        mutableStateOf(false)
    }
    if (functionalityNotAvailablePopupShown) {
        FunctionalityNotAvailablePopup {
            functionalityNotAvailablePopupShown = false
        }
    }

    JetchatAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        onNavIconPressed = onNavIconPressed,
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                //Channel name
                Text(
                    text = channelName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                //number of members
                Text(
                    text = stringResource(id = R.string.members, channelNumbers),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        actions = {
            //Search Icon
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = stringResource(id = R.string.search),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .clickable(onClick = {
                        functionalityNotAvailablePopupShown = true
                    })
                    .padding(horizontal = 12.dp, vertical = 12.dp)
                    .height(24.dp)
            )
            //Info Icon
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = stringResource(id = R.string.info),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .clickable(onClick = {
                        functionalityNotAvailablePopupShown = true
                    })
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .height(24.dp)
            )
        }
    )
}

const val ConversationTestTag = "ConversationTestTag"

@Composable
fun Messages(
    messages: List<Message>,
    navigateToProfile: (String) -> Unit,
    scrollState: LazyListState,
    modifier: Modifier = Modifier
) {

    val scope = rememberCoroutineScope()
    Box(modifier) {
        val authorMe = stringResource(id = R.string.author_me)
        LazyColumn(
            reverseLayout = true, state = scrollState, modifier = Modifier
                .testTag(
                    ConversationTestTag
                )
                .fillMaxSize()
        ) {

            for (index in messages.indices) {
                val prevAuthor = messages.getOrNull(index - 1)?.author
                val nextAuthor = messages.getOrNull(index + 1)?.author
                val content = messages[index]
                val isFirstMessageByAuthor = prevAuthor != content.author
                val isLastMessageByAuthor = nextAuthor != content.author

                //Hardcode day divider for simplicity
                if (index == messages.size - 1) {
                    item {
                        DayHeader(dayString = "20 Aug")
                    }
                } else if (index == 2) {
                    item {
                        DayHeader(dayString = "Today")
                    }
                }

                item {
                    Message(
                        onAuthorClicked = { name -> navigateToProfile(name) },
                        message = content,
                        isUserMe = content.author == authorMe,
                        isFirstMessageByAuthor = isFirstMessageByAuthor,
                        isLastMessageByAuthor = isLastMessageByAuthor
                    )
                }
            }
        }

        //Jump to bottom button shows up when user scrolls past a threshold.
        //convert to pixels
        val jumpThreshold = with(LocalDensity.current) {
            JumpToBottomThreshold.toPx()
        }
        //Show the button if the first visible item is not the first the first one or if the offset is
        //greater than the threshold
        val jumpToBottomButtonEnabled by remember {
            derivedStateOf {
                scrollState.firstVisibleItemIndex != 0 || scrollState.firstVisibleItemScrollOffset > jumpThreshold
            }
        }

        JumpToBottom(enabled = jumpToBottomButtonEnabled, onClicked = {
            scope.launch {
                scrollState.animateScrollToItem(0)
            }
        }, modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
fun Message(
    onAuthorClicked: (String) -> Unit,
    message: Message,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean
) {

    val borderColor =
        if (isUserMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary

    val spaceBetweenAuthors = if (isLastMessageByAuthor) Modifier.padding(top = 8.dp) else Modifier

    Row(modifier = spaceBetweenAuthors) {
        if (isLastMessageByAuthor) {
            //Avatar
            Image(
                modifier = Modifier
                    .clickable(onClick = { onAuthorClicked(message.author) })
                    .padding(horizontal = 16.dp)
                    .size(42.dp)
                    .border(1.5.dp, borderColor, CircleShape)
                    .border(
                        3.dp, MaterialTheme.colorScheme.surface,
                        CircleShape
                    )
                    .clip(CircleShape)
                    .align(Alignment.Top),
                painter = painterResource(id = message.authorImage),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        } else {
            Spacer(modifier = Modifier.width(74.dp))
        }

        AuthorAndTextMessage(
            message = message,
            isUserMe = isUserMe,
            isFirstMessageByAuthor = isFirstMessageByAuthor,
            isLastMessageByAuthor = isFirstMessageByAuthor,
            authorClicked = onAuthorClicked,
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1f)
        )
    }

}

@Composable
fun AuthorAndTextMessage(
    message: Message,
    isUserMe: Boolean,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    authorClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        if (isLastMessageByAuthor) {
            AuthorNameAndTimeStamp(message = message)
        }
        ChatItemBubble(message = message, isUserMe = isUserMe, authorClicked = authorClicked)
        if (isFirstMessageByAuthor) {
            //Last bubble before next author
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            //Between bubbles
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun AuthorNameAndTimeStamp(message: Message) {
    //combine author and time stamp
    Row(modifier = Modifier.semantics(mergeDescendants = true) {}) {
        Text(
            text = message.author,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .alignBy(
                    LastBaseline
                )
                .paddingFrom(LastBaseline, after = 8.dp) //space to first bubble
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = message.timeStamp,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .alignBy(LastBaseline),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

val ChatBubbleShape = RoundedCornerShape(4.dp, 20.dp, 20.dp, 20.dp)

@Composable
fun DayHeader(dayString: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .height(16.dp)
    ) {

        DayHeaderLine()
        Text(
            text = dayString,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        DayHeaderLine()
    }
}

@Composable
fun RowScope.DayHeaderLine() {
    Divider(
        modifier = Modifier
            .weight(1f)
            .align(Alignment.CenterVertically),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
    )
}

@Composable
fun ChatItemBubble(
    message: Message,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit
) {

    val backGroundBubbleColor = if (isUserMe) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column {
        Surface(
            color = backGroundBubbleColor,
            shape = ChatBubbleShape
        ) {

            ClickableMessage(
                message = message,
                isUserMe = isUserMe,
                authorClicked = authorClicked
            )
        }

        message.image?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                color = backGroundBubbleColor,
                shape = ChatBubbleShape
            ) {

                Image(
                    painter = painterResource(id = it),
                    contentDescription = stringResource(R.string.attached_image),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(160.dp)

                )
            }
        }
    }
}

@Composable
fun ClickableMessage(
    message: Message,
    isUserMe: Boolean,
    authorClicked: (String) -> Unit
) {

    val uriHandler = LocalUriHandler.current

    val styledMessage = MessageFormatter(
        text = message.content,
        primary = isUserMe
    )

    ClickableText(
        text = styledMessage,
        style = MaterialTheme.typography.bodyLarge.copy(color = LocalContentColor.current),
        modifier = Modifier.padding(16.dp),
        onClick = {
            styledMessage.getStringAnnotations(start = it, end = it).firstOrNull()
                ?.let { annotation ->
                    when (annotation.tag) {
                        SymbolAnnotationType.PERSON.name -> uriHandler.openUri(annotation.item)
                        SymbolAnnotationType.LINK.name -> uriHandler.openUri(annotation.item)
                        else -> Unit
                    }
                }
        }
    )
}

private val JumpToBottomThreshold = 56.dp