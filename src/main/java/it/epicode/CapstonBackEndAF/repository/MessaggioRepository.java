package it.epicode.CapstonBackEndAF.repository;

import it.epicode.CapstonBackEndAF.model.Messaggio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessaggioRepository extends JpaRepository<Messaggio, Long> {
    Page<Messaggio> findAllByOrderByDataInvioDesc(Pageable pageable);// Trova tutti i messaggi ordinati per dataInvio in ordine decrescente
    Page<Messaggio> findByNome(String nome, Pageable pageable); // Ricerca messaggi per nome
    Page<Messaggio> findByEmail(String email, Pageable pageable);// Ricerca messaggi per email
}
