package it.epicode.CapstonBackEndAF.service;

import it.epicode.CapstonBackEndAF.dto.PrenotazioneDto;
import it.epicode.CapstonBackEndAF.exception.NotFoundException;
import it.epicode.CapstonBackEndAF.model.Prenotazione;
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
        prenotazione.setTelefono (prenotazioneDto.getTelefono());
        prenotazione.setEmail(prenotazioneDto.getEmail());
        prenotazione.setDataRitiro(prenotazioneDto.getDataRitiro());
        // Converto la lista di ID in una lista di oggetti Prodotto prima di passarla al setProdotti()
        List<Long> idsRichiesti = prenotazioneDto.getProdottoIds();
        List<Prodotto> prodotti = prodottoRepository.findAllById(idsRichiesti);
        // Verifico se qualche ID è mancante
        if (prodotti.size() != idsRichiesti.size()) {
            throw new NotFoundException("Uno o più ID prodotto non sono validi");
        }
        prenotazione.setProdotti(prodotti);
        Prenotazione prenotazioneSalvata = prenotazioneRepository.save(prenotazione);

        try {
            emailService.inviaEmailConfermaPrenotazione(
                    prenotazione.getEmail(),
                    prenotazione.getNomeCliente(),
                    prenotazione.getDataRitiro().toString()
            );
        } catch (Exception e) {
            System.err.println("Errore invio email conferma prenotazione: " + e.getMessage());
        }

        return prenotazioneSalvata;
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
    public Prenotazione updatePrenotazione(Long id, PrenotazioneDto prenotazioneDto) throws NotFoundException {
        Prenotazione prenotazioneDaAggiornare = getPrenotazioneById(id);
        prenotazioneDaAggiornare.setNomeCliente(prenotazioneDto.getNomeCliente());
        prenotazioneDaAggiornare.setTelefono(prenotazioneDto.getTelefono());
        prenotazioneDaAggiornare.setEmail(prenotazioneDto.getEmail());
        prenotazioneDaAggiornare.setDataRitiro(prenotazioneDto.getDataRitiro());
        List<Long> idsRichiesti = prenotazioneDto.getProdottoIds();
        List<Prodotto> prodotti = prodottoRepository.findAllById(idsRichiesti);
        if (prodotti.size() != idsRichiesti.size()) {
            throw new NotFoundException("Uno o più ID prodotto non sono validi");
        }
        prenotazioneDaAggiornare.setProdotti(prodotti);
        return prenotazioneRepository.save(prenotazioneDaAggiornare);
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
