package it.epicode.CapstonBackEndAF.service;

import it.epicode.CapstonBackEndAF.dto.PrenotazioneDto;
import it.epicode.CapstonBackEndAF.dto.ProdottoPrenotatoDto;
import it.epicode.CapstonBackEndAF.exception.NotFoundException;
import it.epicode.CapstonBackEndAF.model.Prenotazione;
import it.epicode.CapstonBackEndAF.model.PrenotazioneProdotto;
import it.epicode.CapstonBackEndAF.model.Prodotto;
import it.epicode.CapstonBackEndAF.repository.PrenotazioneRepository;
import it.epicode.CapstonBackEndAF.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Autowired
    private EmailService emailService;

    //Metodo che aggiunge una prenotazione
    public Prenotazione savePrenotazione(PrenotazioneDto prenotazioneDto) throws NotFoundException {
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setNomeCliente(prenotazioneDto.getNomeCliente());
        prenotazione.setTelefono(prenotazioneDto.getTelefono());
        prenotazione.setEmail(prenotazioneDto.getEmail());
        prenotazione.setDataRitiro(prenotazioneDto.getDataRitiro());
        prenotazione.setNote(prenotazioneDto.getNote());


        prenotazione = prenotazioneRepository.save(prenotazione);

        List<PrenotazioneProdotto> prenotazioneProdotti = new ArrayList<>();

        for (ProdottoPrenotatoDto pDto : prenotazioneDto.getProdotti()) {
            Prodotto prodotto = prodottoRepository.findById(pDto.getProdottoId())
                    .orElseThrow(() -> new NotFoundException("Prodotto con ID " + pDto.getProdottoId() + " non trovato"));

            PrenotazioneProdotto pp = new PrenotazioneProdotto();
            pp.setPrenotazione(prenotazione);
            pp.setProdotto(prodotto);
            pp.setQuantita(pDto.getQuantita());
            pp.setTipoQuantita(pDto.getTipoQuantita());

            prenotazioneProdotti.add(pp);
        }

        prenotazione.setPrenotazioneProdotti(prenotazioneProdotti);

        prenotazione = prenotazioneRepository.save(prenotazione);

        try {
            emailService.inviaEmailConfermaPrenotazione(
                    prenotazione.getEmail(),
                    prenotazione.getNomeCliente(),
                    prenotazione.getDataRitiro().toString()
            );

            emailService.inviaNotificaPrenotazioneAlPanificio(prenotazione);

        } catch (Exception e) {
            System.err.println("Errore invio email conferma prenotazione: " + e.getMessage());
        }

        return prenotazione;
    }


    //Metodo per estrarre tutte le prenotazioni
    public Page<Prenotazione> getAllPrenotazione(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return prenotazioneRepository.findAll(pageable);
    }

    //Metodo che restituisce una sola prenotazione
    public Prenotazione getPrenotazioneById(Long id) throws NotFoundException {
        return prenotazioneRepository.findById(id).orElseThrow(() -> new NotFoundException("Prenotazione con id " + id + " non presente"));
    }


    //Metodo per aggiornare una prenotazione
    public Prenotazione updatePrenotazione(Long id, PrenotazioneDto dto) throws NotFoundException {
        Prenotazione prenotazione = getPrenotazioneById(id);

        prenotazione.setNomeCliente(dto.getNomeCliente());
        prenotazione.setTelefono(dto.getTelefono());
        prenotazione.setEmail(dto.getEmail());
        prenotazione.setDataRitiro(dto.getDataRitiro());
        prenotazione.setNote(dto.getNote());

        prenotazione.getPrenotazioneProdotti().clear();


        for (ProdottoPrenotatoDto pDto : dto.getProdotti()) {
            Prodotto prodotto = prodottoRepository.findById(pDto.getProdottoId())
                    .orElseThrow(() -> new NotFoundException("Prodotto con id " + pDto.getProdottoId() + " non trovato"));

            PrenotazioneProdotto prenotazioneProdotto = new PrenotazioneProdotto();
            prenotazioneProdotto.setProdotto(prodotto);
            prenotazioneProdotto.setPrenotazione(prenotazione);
            prenotazioneProdotto.setQuantita(pDto.getQuantita());
            prenotazioneProdotto.setTipoQuantita(pDto.getTipoQuantita());

            prenotazione.getPrenotazioneProdotti().add(prenotazioneProdotto);
        }

        return prenotazioneRepository.save(prenotazione);
    }



    //Metodo per eliminare una prenotazione
    public void deletePrenotazione(Long id) throws NotFoundException {
        Prenotazione prenotazioneDaCancellare = getPrenotazioneById(id);
        prenotazioneRepository.delete(prenotazioneDaCancellare);
    }

    //Metodo per trovare tutte le prenotazioni di un determinato giorno
    public Page<Prenotazione> getPrenotazioniByData(LocalDate data, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return prenotazioneRepository.findByDataRitiro(data, pageable);
    }

    //Metodo per trovare tutte le prenotazioni di un determinato cliente
    public Page<Prenotazione> getPrenotazioniByNomeCliente(String nomeCliente, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return prenotazioneRepository.findByNomeCliente(nomeCliente, pageable);
    }
}
