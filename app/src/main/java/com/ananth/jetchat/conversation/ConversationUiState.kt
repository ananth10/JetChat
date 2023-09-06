package com.ananth.jetchat.conversation

class ConversationUiState(
    val channelName: String,
    val channelMembers: Int,
    initialMessages: List<Message>
) {
    private var messages = initialMessages.toMutableList()

    fun addMessages(msg:Message){
       messages.add(msg)
    }

}