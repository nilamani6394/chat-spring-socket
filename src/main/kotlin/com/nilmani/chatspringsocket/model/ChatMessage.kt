package com.nilmani.chatspringsocket.model

data class ChatMessage(
    var content: String = "",
    var sender: String = "",
    var type: ChatMessage.MessageType? = null
) {
   enum class MessageType {
        CHAT,LEAVE,JOIN
    }
}

