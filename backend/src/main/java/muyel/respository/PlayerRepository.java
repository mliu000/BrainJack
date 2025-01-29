package muyel.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import muyel.model.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {
    Player findByUsername(String username);
}
