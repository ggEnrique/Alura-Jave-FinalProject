package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuarios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var authHeader = request.getHeader("Authorization");

        if(authHeader != null){
            var token = authHeader.replace("Bearer ", "").trim();
            System.out.println("Token after trimming: [" + token + "]"); // Log the token for debugging
            String nombreUsuario = tokenService.getSubject(token);
            System.out.println("Token Subject: " + nombreUsuario);
            if(nombreUsuario != null){
                //Token valido
                var usario = usuarioRepository.findByLogin(nombreUsuario);
                var authentication = new UsernamePasswordAuthenticationToken(usario, null, usario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }
        filterChain.doFilter(request,response);

//        if (token == null || token.trim().isEmpty()){
//            throw new RuntimeException("Token is null or empty");
//        }

//        try {
//
//        } catch (RuntimeException e) {
//            System.err.println("Error: " + e.getMessage());
//            throw e; // Rethrow the exception to prevent further processing
//        }
//
    }
}