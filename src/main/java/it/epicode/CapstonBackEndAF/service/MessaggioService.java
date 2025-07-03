package it.epicode.CapstonBackEndAF.service;

import it.epicode.CapstonBackEndAF.dto.MessaggioDto;
import it.epicode.CapstonBackEndAF.exception.NotFoundException;
import it.epicode.CapstonBackEndAF.model.Messaggio;
import it.epicode.CapstonBackEndAF.repository.MessaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class MessaggioService{

    @Autowired
    private MessaggioRepository messaggioRepository;

    @Autowired
    private EmailService emailService;

    //Metodo che aggiunge un messaggio
    public Messaggio saveMessaggio(MessaggioDto messaggioDto) {
        Messaggio messaggio = new Messaggio();
        messaggio.setNome(messaggioDto.getNome());
        messaggio.setMessaggio(messaggioDto.getMessaggio());
        messaggio.setEmail(messaggioDto.getEmail());

        Messaggio messaggioSalvato = messaggioRepository.save(messaggio);

        try {
            emailService.inviaMessaggioRicevuto(messaggio.getEmail(), messaggio.getNome());
        } catch (Exception e) {
            System.err.println("Errore invio mail: " + e.getMessage());
        }

        return messaggioSalvato;
    }

    //Metodo per estrarre tutti i messaggi
    public Page<Messaggio> getAllMessaggi(int page, int size, String sortBy){
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return messaggioRepository.findAll(pageable);
    }

    //Metodo che restituisce una solo messaggio
    public Messaggio getMessaggioById(Long id) throws NotFoundException {
        return messaggioRepository.findById(id).orElseThrow(() -> new NotFoundException("Messaggio con id " + id + " non presente"));
    }


    //Metodo per aggiornare un messaggio
    public Messaggio updateMessaggio(Long id, MessaggioDto messaggioDto) throws NotFoundException {
        Messaggio messaggioDaAggiornare = getMessaggioById(id);
        messaggioDaAggiornare.setNome(messaggioDto.getNome());
        messaggioDaAggiornare.setMessaggio(messaggioDto.getMessaggio());
        messaggioDaAggiornare.setEmail(messaggioDto.getEmail());
        return messaggioRepository.save(messaggioDaAggiornare);
    }

    //Metodo per eliminare un messaggio
    public void deleteMessaggio(Long id) throws NotFoundException {
        Messaggio messaggioDaCancellare = getMessaggioById(id);
        messaggioRepository.delete(messaggioDaCancellare);
    }

    //Metodo per recuperare tutti i messaggi ordinati per dataInvio decrescente
    public Page<Messaggio> getMessaggiOrdinatiPerDataInvioDesc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return messaggioRepository.findAllByOrderByDataInvioDesc(pageable);
    }

    //Metodo per recuperare messaggi filtrati per nome
    public Page<Messaggio> getMessaggiByNome(String nome, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return messaggioRepository.findByNome(nome, pageable);
    }

    //Metodo per recuperare messaggi filtrati per email
    public Page<Messaggio> getMessaggiByEmail(String email, int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return messaggioRepository.findByEmail(email, pageable);
    }
}
