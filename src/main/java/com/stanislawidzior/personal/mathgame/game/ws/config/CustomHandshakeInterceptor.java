package com.stanislawidzior.personal.mathgame.game.ws.config;

import com.stanislawidzior.personal.mathgame.game.model.GameUser;
import org.jspecify.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.security.Principal;
import java.util.Map;

public class CustomHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if(isUserAuthenticated(request)){
            attributes.put("userId", getGamePlayerFromPrincipal(request.getPrincipal()));
        }
        else if(isUserGuest(request)){

            attributes.put("userId", getGamePlayerFromCookie(request));
        }
        else {
            attributes.put("userId", null);
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {

    }
    private boolean isUserAuthenticated(ServerHttpRequest request){
        return request.getPrincipal() != null;
    }
    private GameUser getGamePlayerFromPrincipal(Principal principal){
        return new GameUser(principal.getName());
    }
    private boolean isUserGuest(ServerHttpRequest request){
        return request.getHeaders().getFirst("Cookie") !=null;
    }
    private String getGamePlayerFromCookie(ServerHttpRequest request){
        var username = request.getHeaders().getFirst("Cookie");
        return username.substring(3);
    }
}
