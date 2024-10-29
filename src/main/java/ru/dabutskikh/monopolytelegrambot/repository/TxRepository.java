package ru.dabutskikh.monopolytelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dabutskikh.monopolytelegrambot.entity.Tx;

@Repository
public interface TxRepository extends JpaRepository<Tx, Long> {
}
