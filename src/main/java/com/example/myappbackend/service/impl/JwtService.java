package com.example.myappbackend.service.impl;

import com.example.myappbackend.exception.ResourceNotFoundException;
import com.example.myappbackend.model.User;
import com.example.myappbackend.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtService {
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;
    @Autowired
    private UserRepository userRepository;

    public String generateToken(UserDetails userDetails) {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role)  // <-- cắt tiền tố ROLE_
                .collect(Collectors.toList());
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", roles) // Thêm roles vào payload
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(jwtExpiration)))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

//    public String generateToken(Authentication authentication) {
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        return generateToken(userDetails);
//    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token);
        return true;
    }

    //    public boolean validateToken(String token, UserDetails userDetails) {
//        String username = Jwts.parserBuilder()
//                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
//                .build()
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject();
//
//        return username.equals(userDetails.getUsername());
//    }
//    private String extractJwtFromCookies(HttpServletRequest request) {
//        if (request.getCookies() == null) return null;
//        for (Cookie cookie : request.getCookies()) {
//            if ("access_token".equals(cookie.getName())) {
//                return cookie.getValue();
//            }
//        }
//        return null;
//    }

//    public User getUserFromRequest(HttpServletRequest request) {
//        String token = Arrays.stream(request.getCookies())
//                .filter(c -> c.getName().equals("access_token"))
//                .findFirst()
//                .map(Cookie::getValue)
//                .orElseThrow(() -> new RuntimeException("Token not found"));
//        String username = extractUsername(token);
//        return userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//    }

    public int extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Người dùng không tồn tại"))
                .getUserId();
    }




    //cho phần đặt hàng
    public String getTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                    .filter(c -> c.getName().equals("access_token"))
                    .map(Cookie::getValue)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}