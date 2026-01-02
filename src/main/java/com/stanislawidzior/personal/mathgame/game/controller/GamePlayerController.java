package com.stanislawidzior.personal.mathgame.game.controller;

import com.stanislawidzior.personal.mathgame.game.exception.UserAlreadyExistsException;
import com.stanislawidzior.personal.mathgame.game.model.GameUser;
import com.stanislawidzior.personal.mathgame.game.service.GameUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GamePlayerController {
    @Autowired
    private GameUserService randomUserService;
    @GetMapping("/game/init-quest")
    public ResponseEntity initGuest(@Payload String userName, HttpServletResponse response) throws UserAlreadyExistsException {
        var id = randomUserService.setNewGuestUserIdentifierReturnId(new GameUser(userName));
        response.addCookie(new Cookie("id", id));
        return ResponseEntity.ok().build();
    }
}
