package com.site.GUFPB.domain.Curso;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String curso;
    private String centro;
    private String sigla;
    @Column(columnDefinition = "TEXT")
    private String dadosTabela;
}