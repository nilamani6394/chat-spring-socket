package com.nilmani.chatspringsocket.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WsConfig : WebSocketMessageBrokerConfigurer  {
    /** WebSocket is configurable so we configure it */
    /**
     * . A contract for registering STOMP over WebSocket endpoints
     * */
    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/firstApp").withSockJS()
    }
    /** withSockJs()
     * Enable SockJS fallback options.
     * */

    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
//        registry.enableSimpleBroker("/topic")
        registry.enableSimpleBroker("/channel")
        registry.setApplicationDestinationPrefixes("/app")

        /**For full access of messageBroker RabbitMq*/
//        registry.enableStompBrokerRelay("/topic")
//            .setRelayHost("127.0.0.1")
//            .setRelayPort(61613)
//            .setClientLogin("guest")
//            .setClientPasscode("guest")

    }

}
