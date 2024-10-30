package ru.dabutskikh.monopolytelegrambot.state_handler;

import ru.dabutskikh.monopolytelegrambot.entity.enums.PlayerGameState;
import ru.dabutskikh.monopolytelegrambot.response.Response;

import java.util.List;

public interface PlayerGameStateHandler {

    PlayerGameState getKey();

    List<Response> execute(GameMoveContext context);
}
