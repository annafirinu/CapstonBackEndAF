package it.epicode.CapstonBackEndAF.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Entity
@Data
@Table(name = "prodotti")
public class Prodotto {
    @Id
    @GeneratedValue
    private Long id;
    private String nome;
    private BigDecimal prezzo;
    private Boolean disponibile;

    @ManyToMany
    @JoinTable(
            name = "prenotazioni_prodotti",
            joinColumns = @JoinColumn(name = "prodotto_id"),
            inverseJoinColumns = @JoinColumn(name = "prenotazione_id")
    )
    private List<Prenotazione> prenotazioni;
}
