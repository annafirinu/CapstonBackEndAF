package it.epicode.CapstonBackEndAF.repository;

import it.epicode.CapstonBackEndAF.model.Prenotazione;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    Page<Prenotazione> findAll(Pageable pageable);// trova tutte le prenotazioni
    Page<Prenotazione> findByDataRitiro(LocalDate dataRitiro, Pageable pageable);// Trova tutte le prenotazioni per un determinato giorno
    Page<Prenotazione> findByNomeCliente(String nome, Pageable pageable);// Ricerca per nome cliente
}
