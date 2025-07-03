package it.epicode.CapstonBackEndAF.repository;

import it.epicode.CapstonBackEndAF.model.Prodotto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
    Page<Prodotto> findByDisponibileTrue(Pageable pageable);// Trova solo i prodotti disponibili
    Page<Prodotto> findByNomeContainingIgnoreCase(String nome, Pageable pageable);// Ricerca per nome (anche parziale)
}