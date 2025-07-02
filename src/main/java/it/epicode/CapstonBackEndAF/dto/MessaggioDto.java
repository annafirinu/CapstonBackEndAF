package it.epicode.CapstonBackEndAF.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class MessaggioDto {
    @NotEmpty(message = "Obbligatorio inserire nome")
    private String nome;
    @Email(message = "Inserire una email valida")
    @NotEmpty(message = "Obbligatorio inserire email")
    private String email;
    @NotEmpty(message = "Obbligatorio inserire messaggio")
    private String messaggio;
}
