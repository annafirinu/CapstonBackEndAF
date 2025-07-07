package it.epicode.CapstonBackEndAF.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class PrenotazioneDto {
    @NotEmpty(message = "Obbligatorio inserire nome")
    private String nomeCliente;
    @NotEmpty(message = "Obbligatorio inserire numero di telefono")
    private String telefono;
    @Email(message = "Inserire una email valida")
    @NotEmpty(message = "Obbligatorio inserire email")
    private String email;
    @NotNull(message = "Obbligatorio inserire la data di ritiro")
    private LocalDate dataRitiro;
    @NotEmpty(message = "Obbligatorio inserire almeno un prodotto")
    private List<ProdottoPrenotatoDto> prodotti;
}
