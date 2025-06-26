package com.elitewheels.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Comprador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cpf;
    private String dataNascimento;
}
