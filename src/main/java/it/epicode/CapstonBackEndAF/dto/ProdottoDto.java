package it.epicode.CapstonBackEndAF.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdottoDto {
    @NotEmpty(message = "Obbligatorio inserire nome")
    private String nome;
    @NotNull(message = "Obbligatorio inserire prezzo")
    @DecimalMin(value = "0.00", inclusive = false, message = "Il prezzo deve essere maggiore di zero")
    private BigDecimal prezzo;
    @NotNull(message = "Il campo 'disponibile' è obbligatorio")
    private Boolean disponibile;
}
