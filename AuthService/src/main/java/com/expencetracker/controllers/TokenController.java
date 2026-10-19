package com.expencetracker.controllers;

import com.expencetracker.entities.RefreshToken;
import com.expencetracker.request.AuthRequestDTO;
import com.expencetracker.request.RefreshTokenRequestDTO;
import com.expencetracker.response.JwtResponseDTO;
import com.expencetracker.services.JwtService;
import com.expencetracker.services.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class TokenController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("auth/v1/login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequestDTO requestDTO) {
        String username = requestDTO.getUsername();
        String password = requestDTO.getPassword();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        if (authentication.isAuthenticated()) {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(username);
            String accessToken = jwtService.generateToken(username);
            return new ResponseEntity<>(JwtResponseDTO.builder().accessToken(accessToken).token(refreshToken.getToken()).build(), HttpStatus.OK);
        }

        return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("auth/v1/refreshToken")
    public ResponseEntity<JwtResponseDTO> refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        JwtResponseDTO response = refreshTokenService.findByRefreshToken(refreshTokenRequestDTO.getRefreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo ->{
                    String accessToken = jwtService.generateToken(userInfo.getUsername());
                    return JwtResponseDTO.builder().accessToken(accessToken).token(refreshTokenRequestDTO.getRefreshToken()).build();
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Refresh Token is invalid or not found in DB !!"));
    return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
