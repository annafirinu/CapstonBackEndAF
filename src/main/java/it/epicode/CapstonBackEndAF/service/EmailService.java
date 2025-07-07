package it.epicode.CapstonBackEndAF.service;

import it.epicode.CapstonBackEndAF.model.Prenotazione;
import it.epicode.CapstonBackEndAF.model.PrenotazioneProdotto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void inviaEmailConfermaPrenotazione(String to, String nomeCliente, String dataRitiro) throws MailException {
        SimpleMailMessage messaggio = new SimpleMailMessage();
        messaggio.setTo(to);
        messaggio.setSubject("Conferma Prenotazione");

        messaggio.setText("Gentile " + nomeCliente + ",\n\n" +
                "La tua prenotazione è stata ricevuta per il giorno " + dataRitiro + ".\n" +
                "Grazie per aver scelto il nostro servizio.");

        mailSender.send(messaggio);
    }

    public void inviaMessaggioRicevuto(String to, String nome) throws MailException {
        SimpleMailMessage messaggio = new SimpleMailMessage();
        messaggio.setTo(to);
        messaggio.setSubject("Messaggio ricevuto");

        messaggio.setText("Ciao " + nome + ",\n\n" +
                "Abbiamo ricevuto il tuo messaggio e ti risponderemo al più presto.\n\n" +
                "Grazie!");

        mailSender.send(messaggio);
    }

    public void inviaMessaggioAlPanificio(String nomeMittente, String emailMittente, String contenutoMessaggio) throws MailException {
        SimpleMailMessage messaggio = new SimpleMailMessage();
        messaggio.setTo("anna.firinu@tiscali.it");
        messaggio.setSubject("Nuovo messaggio da modulo contatti");

        String testo = "Hai ricevuto un nuovo messaggio dal modulo contatti:\n\n" +
                "Nome: " + nomeMittente + "\n" +
                "Email: " + emailMittente + "\n\n" +
                "Messaggio:\n" + contenutoMessaggio;
        messaggio.setText(testo);
        mailSender.send(messaggio);
    }

    public void inviaNotificaPrenotazioneAlPanificio(Prenotazione prenotazione) throws MailException {
        SimpleMailMessage messaggio = new SimpleMailMessage();
        messaggio.setTo("anna.firinu@tiscali.it");
        messaggio.setSubject("Nuova Prenotazione Ricevuta");

        StringBuilder testo = new StringBuilder();
        testo.append("Hai ricevuto una nuova prenotazione:\n\n");
        testo.append("Nome cliente: ").append(prenotazione.getNomeCliente()).append("\n");
        testo.append("Telefono: ").append(prenotazione.getTelefono()).append("\n");
        testo.append("Email: ").append(prenotazione.getEmail()).append("\n");
        testo.append("Data ritiro: ").append(prenotazione.getDataRitiro()).append("\n\n");

        testo.append("Prodotti prenotati:\n");

        for (PrenotazioneProdotto pp : prenotazione.getPrenotazioneProdotti()) {
            testo.append("- ")
                    .append(pp.getProdotto().getNome())
                    .append(" x ")
                    .append(pp.getQuantita())
                    .append(" ")
                    .append(pp.getTipoQuantita())
                    .append("\n");
        }

        messaggio.setText(testo.toString());
        mailSender.send(messaggio);
    }
}

