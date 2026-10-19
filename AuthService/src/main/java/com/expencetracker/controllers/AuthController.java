package com.expencetracker.controllers;

import com.expencetracker.entities.RefreshToken;
import com.expencetracker.modals.UserInfoDto;
import com.expencetracker.response.JwtResponseDTO;
import com.expencetracker.services.JwtService;
import com.expencetracker.services.RefreshTokenService;
import com.expencetracker.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class AuthController {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @PostMapping("auth/v1/signup")
    public ResponseEntity<?> signup(@RequestBody UserInfoDto userInfoDto) {
        try{
            String userId = userDetailsService.signUpUser(userInfoDto);
            if(Objects.isNull(userId)) {
                return new ResponseEntity<>("User already exist", HttpStatus.BAD_REQUEST);
            }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUsername());
            String jwtToken = jwtService.generateToken(userInfoDto.getUsername());
            return new ResponseEntity<>(JwtResponseDTO.builder().accessToken(jwtToken).userId(userId).token(refreshToken.getToken()).build(), HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("Exception in User Service", HttpStatus.INTERNAL_SERVER_ERROR );
        }
    }

}
