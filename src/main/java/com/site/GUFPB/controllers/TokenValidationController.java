package com.site.GUFPB.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TokenValidationController {

    @GetMapping("/validate_token")
    public ResponseEntity<String> validateToken() {
        // Se a requisição chegar aqui, significa que o token foi validado com sucesso pelo Spring Security
        return ResponseEntity.ok("Token é válido");
    }
}