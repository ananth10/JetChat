package com.ananth.jetchat.conversation

import androidx.compose.runtime.toMutableStateList

class ConversationUiState(
    val channelName: String,
    val channelMembers: Int,
    initialMessages: List<Message>
) {
    private val _messages:MutableList<Message> = initialMessages.toMutableStateList()
    val messages:List<Message> = _messages

    fun addMessages(msg:Message){
        _messages.add(0,msg)
    }

}