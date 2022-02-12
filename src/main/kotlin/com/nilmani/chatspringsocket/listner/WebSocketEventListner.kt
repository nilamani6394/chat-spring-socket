package com.nilmani.chatspringsocket.listner

import com.nilmani.chatspringsocket.enum.MessageType
import com.nilmani.chatspringsocket.model.ChatMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import java.lang.String.format


@Component
class WebSocketEventListner {
    val log:Logger = LoggerFactory.getLogger(WebSocketEventListner::class.java)

    @Autowired
    private lateinit var messageingTemplate:SimpMessageSendingOperations

    @EventListener
    fun handleWebSocketListner(event: SessionConnectedEvent){
        log.info("Received a new webSocket Connection")
    }

    fun handleWebSocketDisconnectListner(event: SessionDisconnectEvent){
        val headerAccessor : StompHeaderAccessor = StompHeaderAccessor.wrap(event.message)
        val username:String = headerAccessor.sessionAttributes?.get("username") as String
        val roomId:String = headerAccessor.sessionAttributes?.get("roomId") as  String
        if (username !=null){
            log.info("user Disconnected $username")

            val chatMessage:ChatMessage = ChatMessage()
            chatMessage.type = MessageType.LEAVE
            chatMessage.sender = username

            messageingTemplate!!.convertAndSend(format("/channel/%s", roomId), chatMessage)
        }
    }
}

