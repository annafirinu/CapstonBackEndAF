package it.epicode.CapstonBackEndAF.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "messaggi")
public class Messaggio {
    @Id
    @GeneratedValue
    private Long id;
    private String nome;
    private String email;
    private String messaggio;
    private LocalDate dataInvio;

    @PrePersist
    public void prePersist() {
        this.dataInvio = LocalDate.now();
    }
}
