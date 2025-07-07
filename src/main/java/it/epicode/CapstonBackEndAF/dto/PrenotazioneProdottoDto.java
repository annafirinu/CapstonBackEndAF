package it.epicode.CapstonBackEndAF.dto;

import it.epicode.CapstonBackEndAF.enumeration.TipoQuantita;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrenotazioneProdottoDto {
    @NotNull(message = "Id prodotto obbligatorio")
    private Long prodottoId;
    @NotNull(message = "Quantità obbligatoria")
    @Min(1)
    private Integer quantita;
    @NotNull(message = "Tipo quantità obbligatorio")
    private TipoQuantita tipoQuantita;
}
