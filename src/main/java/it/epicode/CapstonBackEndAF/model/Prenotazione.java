package it.epicode.CapstonBackEndAF.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "prenotazioni")
public class Prenotazione {
    @Id
    @GeneratedValue
    private Long id;
    private String nomeCliente;
    private String telefono;
    private String email;
    private LocalDate dataRitiro;

    @ManyToMany(mappedBy = "prenotazioni")
    @JsonIgnore
    private List<Prodotto> prodotti;
}
