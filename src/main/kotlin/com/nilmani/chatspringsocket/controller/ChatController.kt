package com.nilmani.chatspringsocket.controller

import com.nilmani.chatspringsocket.enum.MessageType
import com.nilmani.chatspringsocket.listner.WebSocketEventListner
import com.nilmani.chatspringsocket.model.ChatMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.stereotype.Controller
import java.lang.String.format


@Controller
class ChatController {

  //  val log:Logger = LoggerFactory.getLogger(WebSocketEventListner::class)
    @Autowired
    private lateinit var  messageingTemplate:SimpMessageSendingOperations

//    @MessageMapping("/chat.register")
//    @SendTo("/topic/public")
//    fun register(@Payload chatMessage: ChatMessage,headerAccessor: SimpMessageHeaderAccessor):ChatMessage{
//        headerAccessor.sessionAttributes!!["username"] = chatMessage.sender
//        return chatMessage
//    }
    @MessageMapping("/chat/{roomId}/sendMessage")
    fun sendMessage(@DestinationVariable roomId:String,@Payload chatMessage: ChatMessage,
    headerAccessor: SimpMessageHeaderAccessor){
        messageingTemplate!!.convertAndSend(format("/channel/%s", roomId), chatMessage)
    }

    /**Add a user*/
    @MessageMapping("/chat/{roomId}/addUser")
    fun addUser(@DestinationVariable roomId: String,@Payload chatMessage: ChatMessage,
    headerAccessor: SimpMessageHeaderAccessor){
        val currentRoomId:String = headerAccessor.sessionAttributes?.put("room_id",roomId) as String
        if (currentRoomId != null){
            val leaveMessage:ChatMessage = ChatMessage()
            leaveMessage.type=MessageType.LEAVE
            leaveMessage.sender = chatMessage.sender
            messageingTemplate.convertAndSend(format("/channel/%s", currentRoomId), leaveMessage)
        }
        headerAccessor.sessionAttributes?.put("usernaem",chatMessage.sender)
        messageingTemplate.convertAndSend(format("/channel/%s", roomId), chatMessage)
    }

    /**
     * SimpleMessageHeaderAccessor.
     *
    A MessageHeaderAccessor to use when creating a Message from a decoded STOMP frame
     * */
//    @MessageMapping("/chat.send")
//    @SendTo("/topic/public")
//    fun sendMessage(@Payload chatMessage: ChatMessage):ChatMessage{
//        return chatMessage
//    }
}

