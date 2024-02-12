package ru.dabutskikh.monopolytelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dabutskikh.monopolytelegrambot.entity.PlayerGame;

import java.util.Optional;

@Repository
public interface PlayerGameRepository extends JpaRepository<PlayerGame, Long> {

    Optional<PlayerGame> findByPlayerIdAndGameId(Long playerId, Long gameId);
}
