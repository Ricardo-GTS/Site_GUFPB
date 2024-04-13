package com.site.GUFPB.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.site.GUFPB.domain.Curso.Curso;


public interface CursoRepository extends JpaRepository<Curso, String> {

    List<Curso> findBySigla(String Sigla);
}
