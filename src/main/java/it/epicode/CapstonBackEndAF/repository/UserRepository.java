package it.epicode.CapstonBackEndAF.repository;

import it.epicode.CapstonBackEndAF.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);//Cerco l'utente in base all'username
    boolean existsByUsername(String username);//Evito username duplicati in fase di registrazione
    boolean existsByEmail(String email);//Evito email duplicate in fase di registrazione
}
