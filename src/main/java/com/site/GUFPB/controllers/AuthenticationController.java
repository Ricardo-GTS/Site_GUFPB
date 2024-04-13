package com.site.GUFPB.controllers;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.site.GUFPB.domain.user.AuthenticationDTO;
import com.site.GUFPB.domain.user.LoginResponseDTO;
import com.site.GUFPB.domain.user.RegisterDTO;
import com.site.GUFPB.domain.user.RegisterResponseDTO;
import com.site.GUFPB.domain.user.User;
import com.site.GUFPB.domain.user.UserRole;
import com.site.GUFPB.infra.security.TokenService;
import com.site.GUFPB.repositories.UserRepository;

import org.springframework.ui.ModelMap;

@Controller
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository repository;
    @Autowired
    private TokenService tokenService;

    @GetMapping("/login")
    public String loginpage(ModelMap model) {
        return "login";
    }

    @PostMapping("/loginpost")
    public ResponseEntity <?> login(@RequestBody @Valid AuthenticationDTO data){
    try {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());

        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));

    } catch (AuthenticationServiceException e) {
        // Retorna uma resposta de erro se a autenticação falhar
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação: " + e.getMessage());
    }
    }

    @GetMapping("/register")
    public String registerpage(ModelMap model) {
        return "register";
    }

    @PostMapping("/register")
    public ResponseEntity <?> register(@RequestBody @Valid RegisterDTO data){
        if(this.repository.findByLogin(data.login()) != null) 
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new RegisterResponseDTO(" Usuário já existe"));
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User newUser = new User(data.login(), encryptedPassword, UserRole.USER);

        this.repository.save(newUser);

        return ResponseEntity.ok(new RegisterResponseDTO("Usuário registrado com sucesso"));

    }

    public void updateUser(User user) {
        this.repository.save(user);
    }
}
