package com.elitewheels.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor; // IMPORTANTE: Adicione este import
import lombok.Data;
import lombok.NoArgsConstructor;   // IMPORTANTE: Adicione este import

@Entity
@Data
@NoArgsConstructor      // Adicione esta anotação
@AllArgsConstructor     // Adicione esta anotação
public class Carro {
    @Id
    private Long codigo;
    private String modelo;
    private String cor;
    private int ano;
    private double valorBase;
}