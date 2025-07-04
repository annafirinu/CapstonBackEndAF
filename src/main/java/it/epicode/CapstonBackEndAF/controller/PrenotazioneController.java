package it.epicode.CapstonBackEndAF.controller;

import it.epicode.CapstonBackEndAF.dto.PrenotazioneDto;
import it.epicode.CapstonBackEndAF.exception.NotFoundException;
import it.epicode.CapstonBackEndAF.exception.ValidationException;
import it.epicode.CapstonBackEndAF.model.Prenotazione;
import it.epicode.CapstonBackEndAF.service.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(path = "/prenotazioni")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    //Creo una prenotazione
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Prenotazione savePrenotazione(@RequestBody @Validated PrenotazioneDto prenotazioneDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage()).
                    reduce("", (e, s) -> e + s));
        }
        return prenotazioneService.savePrenotazione(prenotazioneDto);
    }


    //Richiedo tutte le prenotazioni
    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<Prenotazione> getAllPrenotazioni(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size,
                                                 @RequestParam(defaultValue = "id") String sortBy) {
        return prenotazioneService.getAllPrenotazione(page, size, sortBy);
    }


    //Richiedo una determinata prenotazione tramite l'id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Prenotazione getPrenotazioneById(@PathVariable Long id) throws NotFoundException {
        return prenotazioneService.getPrenotazioneById(id);
    }

    //Modifico una prenotazione esistente
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Prenotazione updatePrenotazione(@PathVariable Long id, @RequestBody @Validated PrenotazioneDto prenotazioneDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage()).
                    reduce("", (e, s) -> e + s));
        }
        return prenotazioneService.updatePrenotazione(id, prenotazioneDto);
    }

    //Elimino una prenotazione
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void deletePrenotazione(@PathVariable Long id) throws NotFoundException {
        prenotazioneService.deletePrenotazione(id);
    }

    //Ricerco le prenotazioni di un determinato giorno
    @GetMapping("/search/data")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<Prenotazione> getPrenotazioniByData(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                                                    @RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @RequestParam(defaultValue = "id") String sortBy) {
        return prenotazioneService.getPrenotazioniByData(data, page, size, sortBy);
    }

    //Ricerco tutte le prenotazioni di un determinato cliente
    @GetMapping("/search/nome-cliente")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<Prenotazione> getPrenotazioniByNomeCliente(@RequestParam String nomeCliente,
                                                           @RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size,
                                                           @RequestParam(defaultValue = "id") String sortBy) {
        return prenotazioneService.getPrenotazioniByNomeCliente(nomeCliente, page, size, sortBy);
    }

}
