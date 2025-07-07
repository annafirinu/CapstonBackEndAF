package it.epicode.CapstonBackEndAF.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.epicode.CapstonBackEndAF.enumeration.TipoQuantita;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "prenotazioni_prodotti")
public class PrenotazioneProdotto {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "prenotazione_id", nullable = false)
    private Prenotazione prenotazione;

    @ManyToOne
    @JoinColumn(name = "prodotto_id", nullable = false)
    private Prodotto prodotto;

    private Integer quantita;

    @Enumerated(EnumType.STRING)
    private TipoQuantita tipoQuantita;
}
