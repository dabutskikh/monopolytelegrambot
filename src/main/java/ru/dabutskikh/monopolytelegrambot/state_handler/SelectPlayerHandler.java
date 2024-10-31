package ru.dabutskikh.monopolytelegrambot.state_handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dabutskikh.monopolytelegrambot.entity.enums.PlayerGameState;
import ru.dabutskikh.monopolytelegrambot.exception.UserException;
import ru.dabutskikh.monopolytelegrambot.response.Response;
import ru.dabutskikh.monopolytelegrambot.service.PlayerService;

import java.util.List;

//@Component
//@RequiredArgsConstructor
public class SelectPlayerHandler /*implements PlayerGameStateHandler*/ {

//    private final PlayerService playerService;
//
//    @Override
//    public PlayerGameState getKey() {
//        return PlayerGameState.SELECT_PLAYER;
//    }
//
//    @Override
//    public List<Response> execute(GameMoveContext context) {
//        Long gameId = context.getPlayer().getCurrentGameId();
//        playerService.findActiveGamePlayers(gameId).stream()
//                .filter(player -> player.getUsername().equals(context.getCommand().getText()))
//                .findFirst()
//                .orElseThrow(() -> new UserException("Игрок " + context.getCommand().getText()));
//        return null;
//    }
}
