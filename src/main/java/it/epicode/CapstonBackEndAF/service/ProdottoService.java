package it.epicode.CapstonBackEndAF.service;

import com.cloudinary.Cloudinary;
import it.epicode.CapstonBackEndAF.dto.ProdottoDto;
import it.epicode.CapstonBackEndAF.exception.NotFoundException;
import it.epicode.CapstonBackEndAF.model.Prodotto;
import it.epicode.CapstonBackEndAF.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;

@Service
public class ProdottoService {

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Autowired
    private Cloudinary cloudinary;

    //Metodo che aggiunge un prodotto
    public Prodotto saveProdotto(ProdottoDto prodottoDto) {
        Prodotto prodotto = new Prodotto();
        prodotto.setNome(prodottoDto.getNome());
        prodotto.setDescrizione(prodottoDto.getDescrizione());
        prodotto.setAllergeni(prodottoDto.getAllergeni());
        prodotto.setPrezzo(prodottoDto.getPrezzo());
        prodotto.setDisponibile(prodottoDto.getDisponibile());
        return prodottoRepository.save(prodotto);
    }

    //Metodo per estrarre tutti i prodotti
    public Page<Prodotto> getAllProdotti(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return prodottoRepository.findAll(pageable);
    }

    //Metodo che restituisce una solo prodotto
    public Prodotto getProdottoById(Long id) throws NotFoundException {
        return prodottoRepository.findById(id).orElseThrow(() -> new NotFoundException("Prodotto con id " + id + " non presente"));
    }


    //Metodo per aggiornare un prodotto
    public Prodotto updateProdotto(Long id, ProdottoDto prodottoDto) throws NotFoundException {
        Prodotto prodottoDaAggiornare = getProdottoById(id);
        prodottoDaAggiornare.setNome(prodottoDto.getNome());
        prodottoDaAggiornare.setDescrizione(prodottoDto.getDescrizione());
        prodottoDaAggiornare.setAllergeni(prodottoDto.getAllergeni());
        prodottoDaAggiornare.setPrezzo(prodottoDto.getPrezzo());
        prodottoDaAggiornare.setDisponibile(prodottoDto.getDisponibile());
        return prodottoRepository.save(prodottoDaAggiornare);
    }

    //Metodo per eliminare un prodotto
    public void deleteProdotto(Long id) throws NotFoundException {
        Prodotto prodottoDaCancellare = getProdottoById(id);
        prodottoRepository.delete(prodottoDaCancellare);
    }

    //Metodo per aggiungere l'immagine al prodotto
    public String patchProdotto(Long id, MultipartFile file) throws NotFoundException, IOException {
        Prodotto prodottoDaPatchare = getProdottoById(id);
        String immagineProdotto = (String) cloudinary.uploader().upload(file.getBytes(), Collections.emptyMap()).get("url");

        prodottoDaPatchare.setImmagineProdotto(immagineProdotto);
        prodottoRepository.save(prodottoDaPatchare);
        return immagineProdotto;
    }

    //Metodo che trova solo i prodotti disponibili
    public Page<Prodotto> getProdottiDisponibili(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return prodottoRepository.findByDisponibileTrue(pageable);
    }

    //Metodo che cerca i prodotti per nome(anche parziale)
    public Page<Prodotto> searchProdottiByNome(String nome, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return prodottoRepository.findByNomeContainingIgnoreCase(nome, pageable);
    }

}
