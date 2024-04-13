package com.site.GUFPB.services;

import java.io.*;
import java.nio.charset.StandardCharsets;
import org.apache.commons.csv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.site.GUFPB.domain.Curso.Curso;
import com.site.GUFPB.repositories.CursoRepository;

import jakarta.annotation.PostConstruct;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @PostConstruct
    public void readCSVAndSaveData() {
        InputStream is = getClass().getResourceAsStream("/cursos_info.csv");
        try (Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim())) {

            for (CSVRecord record : csvParser) {
                Curso curso = new Curso();
                curso.setCurso(record.get("Curso"));
                curso.setCentro(record.get("Centro"));
                curso.setSigla(record.get("Sigla"));
                curso.setDadosTabela(record.get("Dados da Tabela"));
                cursoRepository.save(curso);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
