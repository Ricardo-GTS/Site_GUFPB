package com.site.GUFPB.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.site.GUFPB.domain.Curso.Curso;
import com.site.GUFPB.domain.user.User;
import com.site.GUFPB.repositories.CursoRepository;


@Controller
@RequestMapping("/recomendar")
public class RecomendationController {

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    public String recomendar(ModelMap model) {
        return "recomendar";
    }

    @GetMapping("/cursos")
    public ResponseEntity<?> recomendarCursos() {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha na autenticação: ");
        }
        User user = (User) authentication.getPrincipal();
        String sigla = user.getCentro_Recomendado();
        if (sigla == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Centro recomendado não encontrado");
        }

        List<Curso> cursos = cursoRepository.findBySigla(sigla);
        if (cursos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum curso encontrado para o centro: " + sigla);
        }

        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/todos")
    public String todos(ModelMap model) {
        return "todoscursos";       
    }

    @GetMapping("/todos/cursos")
    public ResponseEntity<?> TodosCursos() {
        
        List<Curso> cursos = cursoRepository.findAll();
        if (cursos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum curso encontrado para o centro: ");
        }

        return ResponseEntity.ok(cursos);
    }

}
