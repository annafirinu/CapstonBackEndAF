package it.epicode.CapstonBackEndAF.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    private String immagineProdotto;

    @OneToMany(mappedBy = "prodotto")
    @JsonIgnore
    private List<PrenotazioneProdotto> prenotazioneProdotti = new ArrayList<>();
    //private List<Prenotazione> prenotazioni;
}
