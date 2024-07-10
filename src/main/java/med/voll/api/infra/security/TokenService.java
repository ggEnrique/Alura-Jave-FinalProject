package med.voll.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import med.voll.api.domain.usuarios.Usuario;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    public final String secret = System.getenv("apiSecret");

    public String generarToken(Usuario usuario){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("voll med")
                    .withSubject(usuario.getLogin())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(generarFechaExpiracion())
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException();
        }
    }

    public String getSubject(String token) {
        if (token == null){
            throw new RuntimeException("Token is null or empty");
        }
        DecodedJWT verifier = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            verifier = JWT.require(algorithm)
                    .withIssuer("voll med")
                    .build()
                    .verify(token);
            verifier.getSubject();
        } catch (JWTVerificationException exception) {
            System.out.println(exception.toString());
        }

        if (verifier.getSubject() == null){
            throw new RuntimeException("Verifier invalido");
        }
        return verifier.getSubject();
    }

    private Instant generarFechaExpiracion(){
        return LocalDateTime.now().plusHours(10).toInstant(ZoneOffset.of("-05:00"));
    }
}



//@Service
//public class TokenService {
//
//    public final String secret = System.getenv("apiSecret");
//
//    public String generarToken(Usuario usuario) {
//        if (secret == null || secret.isEmpty()) {
//            throw new RuntimeException("Secret key is not set");
//        }
//
//        try {
//            Algorithm algorithm = Algorithm.HMAC256(secret);
//            return JWT.create()
//                    .withIssuer("voll med")
//                    .withSubject(usuario.getLogin())
//                    .withClaim("id", usuario.getId())
//                    .withExpiresAt(generarFechaExpiracion())
//                    .sign(algorithm);
//        } catch (JWTCreationException exception) {
//            throw new RuntimeException("Error creating JWT token", exception);
//        }
//    }
//
//    public String getSubject(String token) {
//        if (token == null || token.trim().isEmpty()) {
//            throw new RuntimeException("Token is null or empty");
//        }
//
//        token = token.trim(); // Ensure there are no leading or trailing spaces
//
//        System.out.println("Token: [" + token + "]"); // Log token for debugging
//
//        DecodedJWT verifier;
//        try {
//            Algorithm algorithm = Algorithm.HMAC256(secret);
//            verifier = JWT.require(algorithm)
//                    .withIssuer("voll med")
//                    .build()
//                    .verify(token);
//        } catch (JWTVerificationException exception) {
//            throw new RuntimeException("Error verifying JWT token", exception);
//        }
//
//        String subject = verifier.getSubject();
//        if (subject == null) {
//            throw new RuntimeException("Invalid token: subject is null");
//        }
//        return subject;
//    }
//
//    private Instant generarFechaExpiracion() {
//        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));
//    }
//}
