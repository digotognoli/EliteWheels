package com.elitewheels.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Vendedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // <-- ADICIONE ESTA LINHA
    private Long id;
    private String nome;
    private String senha;
}