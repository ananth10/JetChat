package com.ananth.jetchat.conversation

class ConversationUiState(
    val channelName: String,
    val channelMembers: Int,
    initialMessages: List<Message>
) {
    private var _messages = initialMessages.toMutableList()
    val messages:List<Message> = _messages

    fun addMessages(msg:Message){
        _messages.add(msg)
    }

}