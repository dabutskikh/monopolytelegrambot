package ru.dabutsikh.monopolytelegrambot.service.interfaces;

import ru.dabutsikh.monopolytelegrambot.model.Game;
import ru.dabutsikh.monopolytelegrambot.model.Player;

import java.util.List;

public interface PlayerService {

//    Player findById(Long playerId);

    Player saveOrUpdate(Long playerId, String username, String firstName, String lastName);

    List<Player> getActivePlayersByGame(Game game);
}
