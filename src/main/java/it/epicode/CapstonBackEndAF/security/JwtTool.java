package it.epicode.CapstonBackEndAF.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.epicode.CapstonBackEndAF.exception.NotFoundException;
import it.epicode.CapstonBackEndAF.model.User;
import it.epicode.CapstonBackEndAF.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTool {

    @Value("${jwt.duration}")
    private long durata;

    @Value("${jwt.secret}")
    private String chiaveSegreta;

    @Autowired
    private UserService userService;

    public String createToken(User user){


        //creazione del token
        return Jwts.builder().issuedAt(new Date()).
                expiration(new Date(System.currentTimeMillis()+durata)).
                subject(String.valueOf(user.getId())).
                signWith(Keys.hmacShaKeyFor(chiaveSegreta.getBytes())).
                compact();
    }


    //metodo per la verifica della validità del token
    public void validateToken(String token){
        Jwts.parser().verifyWith(Keys.hmacShaKeyFor(chiaveSegreta.getBytes())).
                build().parse(token);
    }


    //metodo per estrarre l'utente collegato al token
    public User getUserFromToken(String token) throws NotFoundException {
        //recuperare l'id dell'utente dal token
        int id = Integer.parseInt(Jwts.parser().verifyWith(Keys.hmacShaKeyFor(chiaveSegreta.getBytes())).
                build().parseSignedClaims(token).getPayload().getSubject());

        return userService.getUser(id);
    }

}
