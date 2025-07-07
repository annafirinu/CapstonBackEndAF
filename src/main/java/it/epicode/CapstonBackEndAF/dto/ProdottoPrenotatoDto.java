package it.epicode.CapstonBackEndAF.dto;

import it.epicode.CapstonBackEndAF.enumeration.TipoQuantita;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProdottoPrenotatoDto {
    private Long prodottoId;
    @NotNull(message = "Obbligatorio inserire quantità")
    private int quantita;
    @NotNull(message = "Obbligatorio inserire KG o PEZZO")
    private TipoQuantita tipoQuantita;
}

