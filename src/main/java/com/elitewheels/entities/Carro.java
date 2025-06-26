package com.elitewheels.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Carro {
    @Id
    private Long codigo;
    private String modelo;
    private String cor;
    private int ano;
    private double valorBase;
}
