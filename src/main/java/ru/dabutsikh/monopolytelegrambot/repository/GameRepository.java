package ru.dabutsikh.monopolytelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.dabutsikh.monopolytelegrambot.model.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
}
