package ru.dabutsikh.monopolytelegrambot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "player")
@NoArgsConstructor
@Data
public class Player {

    @Id
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @OneToMany(
            orphanRemoval = true,
            mappedBy = "winner"
    )
    private List<Game> wonGames;

    @OneToMany(
            orphanRemoval = true,
            mappedBy = "creator"
    )
    private List<Game> createdGames;

    @OneToMany(
            mappedBy = "id.player"
    )
    private List<PlayerGame> playerGames;

    public Player(Long id, String username, String firstName, String lastName) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id.equals(player.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
