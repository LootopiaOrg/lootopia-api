package com.lootopiaApi.service.impl;

import com.lootopiaApi.model.entity.User;
import com.lootopiaApi.repository.UserRepository;
import com.lootopiaApi.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class JWTServiceImpl implements JWTService {

    private final String key = "jxgEQeXHuPq8VdbyYFNkANdudQ53YUn4";
    private final SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));

    private final UserRepository userRepository;

    public JWTServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String generateJwt(String username) throws ParseException {
        Date date= new Date();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));
        var roles = user.getRoles().stream()
                .map(role -> "ROLE_" + role.getRole())
                .toList();

        return Jwts.builder()
                .setIssuer("MFA Server")
                .setSubject(username)
                .claim("username", username)
                .claim("roles", roles)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + 8 * 60 * 60 * 1000))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public Authentication validateJwt(String jwt) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwt)
                .getBody();

        String username = claims.get("username", String.class);
        List<String> roles = claims.get("roles", List.class); // roles from token

        if (username != null && roles != null) {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            List<SimpleGrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            return new UsernamePasswordAuthenticationToken(user, null, authorities);
        }

        return null;
    }

}
