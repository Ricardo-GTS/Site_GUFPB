package com.site.GUFPB.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.site.GUFPB.domain.user.QuestionarioDTO;
import com.site.GUFPB.domain.user.User;
import com.site.GUFPB.repositories.UserRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/questionario")
public class QuestionarioController {

    @Autowired
    private UserRepository repository;

    @GetMapping
    public String questinarioPage(ModelMap model) {
        return "questionario";
    }

    @PostMapping("/resposta")
    public ResponseEntity <?> resposta(@RequestBody @Valid QuestionarioDTO resposta) {
        // Obter o usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação: ");
        }
        User user = (User) authentication.getPrincipal();
        user.setCentro_Recomendado(resposta.areaEscolhida());

        // Salvar as alterações no usuário
        this.repository.save(user);

        return ResponseEntity.ok("OK");
    }
}
