package it.epicode.CapstonBackEndAF.security;

import it.epicode.CapstonBackEndAF.exception.NotFoundException;
import it.epicode.CapstonBackEndAF.exception.UnAuthorizedException;
import it.epicode.CapstonBackEndAF.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component

public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTool jwtTool;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new UnAuthorizedException("Token non presente, non sei autorizzato ad usare il servizio richiesto");
        } else {
            //estraggo il token
            String token = authorization.substring(7);

            //verifico che il token sia valido
            jwtTool.validateToken(token);

            try {
                //recupero l'utente collegato al token usando il metodo getUserFromToken del jwtTool
                User user = jwtTool.getUserFromToken(token);

                //creo un oggetto authentication inserendogli all'interno l'utente recuperato e il suo ruolo
                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                //aggiungo l'autenticazione con l'utente nel contesto di Spring security
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (NotFoundException e) {
                throw new UnAuthorizedException("Utente collegato al token non trovato");
            }


            filterChain.doFilter(request, response);
        }

    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String[] excludedEndpoints = new String[]{"/auth/**", "/html/**"};

        return Arrays.stream(excludedEndpoints)
                .anyMatch(e -> new AntPathMatcher().match(e, request.getServletPath()));
    }
}
