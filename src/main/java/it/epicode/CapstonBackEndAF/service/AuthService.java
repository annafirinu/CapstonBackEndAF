package it.epicode.CapstonBackEndAF.service;

import it.epicode.CapstonBackEndAF.dto.LoginDto;
import it.epicode.CapstonBackEndAF.exception.NotFoundException;
import it.epicode.CapstonBackEndAF.model.User;
import it.epicode.CapstonBackEndAF.repository.UserRepository;
import it.epicode.CapstonBackEndAF.security.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String login(LoginDto loginDto) throws NotFoundException {
        User user = userRepository.findByUsername(loginDto.getUsername()).
                orElseThrow(() -> new NotFoundException("Utente con questo username/password non trovato"));

        if(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){

            return jwtTool.createToken(user);
        }
        else{
            throw new NotFoundException("Utente con questo username/password non trovato");
        }
    }
}
