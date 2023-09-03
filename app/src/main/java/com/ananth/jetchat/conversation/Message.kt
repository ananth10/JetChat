package com.ananth.jetchat.conversation

import androidx.compose.runtime.Immutable
import com.ananth.jetchat.R

@Immutable
data class Message(
    val author: String,
    val content: String,
    val timeStamp: String,
    val image: Int? = null,
    val authorImage: Int = if (author == "me") R.drawable.ali else R.drawable.someone_else
)