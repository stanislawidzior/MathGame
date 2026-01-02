package com.stanislawidzior.personal.mathgame.game.ws.config;

import com.stanislawidzior.personal.mathgame.game.auth.model.AppUser;
import com.stanislawidzior.personal.mathgame.game.auth.model.UserAuthority;
import com.stanislawidzior.personal.mathgame.game.auth.model.UserRole;
import com.stanislawidzior.personal.mathgame.game.model.GamePlayer;
import com.stanislawidzior.personal.mathgame.game.service.GameUserService;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if(isUserAuthenticated(request)){
            attributes.put("user", getGamePlayerFromPrincipal(request.getPrincipal()));
        }
        else if(isUserGuest(request)){

            attributes.put("user", );
        }
        else {
            attributes.put("user", null);
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {

    }
    private boolean isUserAuthenticated(ServerHttpRequest request){
        return request.getPrincipal() != null;
    }
    private GamePlayer getGamePlayerFromPrincipal(Principal principal){
        return new GamePlayer();
    }
    private boolean isUserGuest(ServerHttpRequest request){
        return request.getHeaders().getFirst("Cookie") !=null;
    }
    private AppUser getGuestUserFromCookie(ServerHttpRequest request){
        var username = request.getHeaders().getFirst("Cookie");
        return new AppUser(username, null, List.of(new UserAuthority(UserRole.GUEST)));
    }
}
