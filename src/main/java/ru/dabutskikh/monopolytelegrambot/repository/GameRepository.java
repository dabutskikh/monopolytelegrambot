package ru.dabutskikh.monopolytelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dabutskikh.monopolytelegrambot.entity.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}
