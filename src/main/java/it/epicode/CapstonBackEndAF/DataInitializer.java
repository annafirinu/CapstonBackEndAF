package it.epicode.CapstonBackEndAF;

import it.epicode.CapstonBackEndAF.enumeration.Ruolo;
import it.epicode.CapstonBackEndAF.model.User;
import it.epicode.CapstonBackEndAF.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Controlla se esiste almeno un admin
        boolean adminExists = userRepository.findAll().stream()
                .anyMatch(user -> user.getRuolo() == Ruolo.ADMIN);

        if (!adminExists) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@panificio.it");
            admin.setNome("Admin");
            admin.setCognome("Panificio");
            admin.setRuolo(Ruolo.ADMIN);

            userRepository.save(admin);
            System.out.println("Primo admin creato: username='admin', password='admin123'");
        }
    }
}
