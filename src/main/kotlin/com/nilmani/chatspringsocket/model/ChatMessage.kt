package com.nilmani.chatspringsocket.model

import com.nilmani.chatspringsocket.enum.MessageType

data class ChatMessage(
    var content: String = "",
    var sender: String = "",
    var recipient : String="",
    var type: MessageType? = null
)
//{
//   enum class MessageType {
//        CHAT,LEAVE,JOIN
//            ChatMessage.
//    }
//}

