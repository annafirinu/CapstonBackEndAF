package it.epicode.CapstonBackEndAF.controller;

import it.epicode.CapstonBackEndAF.dto.MessaggioDto;
import it.epicode.CapstonBackEndAF.exception.NotFoundException;
import it.epicode.CapstonBackEndAF.exception.ValidationException;
import it.epicode.CapstonBackEndAF.model.Messaggio;
import it.epicode.CapstonBackEndAF.service.MessaggioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/messaggi")
public class MessaggioController {

    @Autowired
    private MessaggioService messaggioService;

    //Creo un Messaggio
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Messaggio saveMessaggio(@RequestBody @Validated MessaggioDto messaggioDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage()).
                    reduce("", (e, s) -> e + s));
        }
        return messaggioService.saveMessaggio(messaggioDto);
    }


    //Richiedo tutti i messaggi presenti
    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<Messaggio> getAllMessaggi(@RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "100") int size,
                                          @RequestParam(defaultValue = "id") String sortBy) {
        return messaggioService.getAllMessaggi(page, size, sortBy);
    }


    //Richiedo un determinato messaggio tramite l'id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Messaggio getMessaggioById(@PathVariable Long id) throws NotFoundException {
        return messaggioService.getMessaggioById(id);
    }

    //Modifico un messaggio esistente
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Messaggio updateMessaggio(@PathVariable Long id, @RequestBody @Validated MessaggioDto messaggioDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage()).
                    reduce("", (e, s) -> e + s));
        }
        return messaggioService.updateMessaggio(id, messaggioDto);
    }

    //Elimino un messaggio
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void deleteMessaggio(@PathVariable Long id) throws NotFoundException {
        messaggioService.deleteMessaggio(id);
    }

    //Faccio una ricerca per nome
    @GetMapping("/search/nome")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<Messaggio> getMessaggiByNome(@RequestParam String nome,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size,
                                             @RequestParam(defaultValue = "id") String sortBy) {
        return messaggioService.getMessaggiByNome(nome, page, size, sortBy);
    }

    //Faccio una ricerca per email
    @GetMapping("/search/email")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<Messaggio> getMessaggiByEmail(@RequestParam String email,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              @RequestParam(defaultValue = "id") String sortBy) {
        return messaggioService.getMessaggiByEmail(email, page, size, sortBy);
    }

    //Richiedo una lista ordinata per dataInvio discendente
    @GetMapping("/ordered")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<Messaggio> getMessaggiOrdinatiPerDataInvioDesc(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        return messaggioService.getMessaggiOrdinatiPerDataInvioDesc(page, size);
    }


}
