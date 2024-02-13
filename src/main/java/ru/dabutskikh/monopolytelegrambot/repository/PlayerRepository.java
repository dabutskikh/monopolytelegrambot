package ru.dabutskikh.monopolytelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dabutskikh.monopolytelegrambot.entity.Player;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByTelegramId(Long telegramId);

    List<Player> findByCurrentGameId(Long gameId);
}
