package com.inno.product.service.auth;


import com.inno.product.entity.UserDTO;
import com.inno.product.exception.ApiRequestException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Service
public class AuthService {
    @Value("${jwt.secret}")
    private String JWT_SECRET;
    public UserDTO verifyToken(String token) {
        try {
            UserDTO user = new UserDTO();
            Claims claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token)
                    .getBody();
            user.setId(Integer.parseInt(claims.get("id").toString()));
            user.setEmail(claims.get("email").toString());
            user.setRole((claims.get("role").toString()));
            return user;
        } catch (Exception e) {
                throw new ApiRequestException("invalid_request", HttpStatus.UNAUTHORIZED);
        }
    }
    public boolean checkRoleAdmin(UserDTO userDTO) {
        return "ADMIN".equalsIgnoreCase(userDTO.getRole());
    }
}
