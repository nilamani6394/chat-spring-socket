package com.nilmani.chatspringsocket.controller

import com.nilmani.chatspringsocket.model.ChatMessage
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller


@Controller
class ChatController {
    @MessageMapping("/chat.register")
    @SendTo("/topic/public")
    fun register(@Payload chatMessage: ChatMessage,headerAccessor: SimpMessageHeaderAccessor):ChatMessage{
        headerAccessor.sessionAttributes!!["username"] = chatMessage.sender
        return chatMessage
    }
    @MessageMapping("/chat.send")
    @SendTo("/topic/public")
    fun sendMessage(@Payload chatMessage: ChatMessage):ChatMessage{
        return chatMessage
    }
}

