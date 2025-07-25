package it.epicode.CapstonBackEndAF.controller;

import it.epicode.CapstonBackEndAF.dto.ProdottoDto;
import it.epicode.CapstonBackEndAF.exception.NotFoundException;
import it.epicode.CapstonBackEndAF.exception.ValidationException;
import it.epicode.CapstonBackEndAF.model.Prodotto;
import it.epicode.CapstonBackEndAF.service.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(path = "/prodotti")
public class ProdottoController {

    @Autowired
    private ProdottoService prodottoService;

    //Creo un Prodotto
    @PostMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public Prodotto saveProdotto(@RequestBody @Validated ProdottoDto prodottoDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage()).
                    reduce("", (e, s) -> e + s));
        }
        return prodottoService.saveProdotto(prodottoDto);
    }


    //Richiedo tutti i prodotti presenti
    @GetMapping("")
    public Page<Prodotto> getAllProdotti(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "100") int size,
                                     @RequestParam(defaultValue = "id") String sortBy) {
        return prodottoService.getAllProdotti(page, size, sortBy);
    }


    //Richiedo un determinato prodotto tramite l'id
    @GetMapping("/{id}")
    public Prodotto getProdotto(@PathVariable Long id) throws NotFoundException {
        return prodottoService.getProdottoById(id);
    }

    //Modifico un prodotto esistente
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Prodotto updateProdotto(@PathVariable Long id, @RequestBody @Validated ProdottoDto prodottoDto, BindingResult bindingResult) throws NotFoundException, ValidationException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult.getAllErrors().stream().map(objectError -> objectError.getDefaultMessage()).
                    reduce("", (e, s) -> e + s));
        }
        return prodottoService.updateProdotto(id, prodottoDto);
    }

    //Elimino un prodotto
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void deleteProdotto(@PathVariable Long id) throws NotFoundException {
        prodottoService.deleteProdotto(id);
    }

    //Aggiungo l'immagine del prodotto
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('ADMIN')")
    public String patchProdotto(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws NotFoundException, IOException {
        return prodottoService.patchProdotto(id, file);
    }

    //Richiedo solo i prodotti disponibili
    @GetMapping("/disponibili")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<Prodotto> getProdottiDisponibili(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "100") int size,
                                                 @RequestParam(defaultValue = "id") String sortBy) {
        return prodottoService.getProdottiDisponibili(page, size, sortBy);
    }

    //Ricerco prodotti per nome (anche parziale)
    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<Prodotto> searchProdottiByNome(@RequestParam String nome,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               @RequestParam(defaultValue = "id") String sortBy) {
        return prodottoService.searchProdottiByNome(nome, page, size, sortBy);
    }

}

