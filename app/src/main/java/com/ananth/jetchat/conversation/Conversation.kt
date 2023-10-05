package com.ananth.jetchat.conversation

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.ananth.jetchat.R

/**
 * Entry point for conversation screen
 *
 * @param uiState[ConversationUiState] that contains messages to display
 * @param navigateToProfile User action when navigation when navigation to profile is requested
 * @param modifier [Modifier] to apply to this layout node
 * @param onNavIconPressed Sends an event up when the user clicks on the menu
 * */

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

//    Scaffold {
//
//    }
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