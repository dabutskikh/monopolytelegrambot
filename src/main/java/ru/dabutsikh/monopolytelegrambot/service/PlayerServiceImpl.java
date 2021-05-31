package ru.dabutsikh.monopolytelegrambot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.dabutsikh.monopolytelegrambot.model.Game;
import ru.dabutsikh.monopolytelegrambot.model.Player;
import ru.dabutsikh.monopolytelegrambot.repository.PlayerRepository;
import ru.dabutsikh.monopolytelegrambot.service.interfaces.PlayerService;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    PlayerRepository playerRepository;

//    @Override
//    public Player findById(Long playerId) {
//        return playerRepository.findById(playerId).orElseThrow(
//                () -> new RuntimeException("Игрока с id " + playerId + " не существует")
//        );
//    }

    @Override
    public Player saveOrUpdate(Long playerId, String username) {
        if (username == null) {
            username = "Noname (id " + playerId.toString() + ")";
        }
        Optional<Player> playerOpt = playerRepository.findById(playerId);
        Player player;
        if (playerOpt.isPresent()) {
            player = playerOpt.get();
            player.setUsername(username);
//            player.setFirstName(firstName);
//            player.setLastName(lastName);
            return playerRepository.saveAndFlush(player);
        } else {
            player = new Player(playerId, username);
        }
        return playerRepository.saveAndFlush(player);
    }

    @Override
    public List<Player> getActivePlayersByGame(Game game) {
        return playerRepository.getActivePlayersByGameId(game.getId());
    }
}
