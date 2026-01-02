package com.stanislawidzior.personal.mathgame.game.ws.config;

import com.stanislawidzior.personal.mathgame.game.service.GameUserService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class SubscribeListener implements ApplicationListener<SessionSubscribeEvent> {

    private final SimpMessagingTemplate messagingTemplate;
    private final GameUserService randomUserService;


    public SubscribeListener(SimpMessagingTemplate messagingTemplate, GameUserService randomUserService) {
        this.messagingTemplate = messagingTemplate;
        this.randomUserService = randomUserService;
    }

    @Override
    public void onApplicationEvent(SessionSubscribeEvent event) {
        messagingTemplate.convertAndSend("/game/room", "Hello");
    }

    @EventListener
    public void onDisconnect(SessionDisconnectEvent event) {
        try {
            randomUserService.removeGamePlayerFromSession(event.getSessionId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        messagingTemplate.convertAndSend("/game/room", "Goodbye");
    }
}
