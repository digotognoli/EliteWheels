package com.elitewheels.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Venda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Vendedor vendedor;

    @ManyToOne
    private Comprador comprador;

    @ManyToOne
    private Carro carro;

    private String dataHora;
    private double desconto;
    private double valorFinal;
    private String formaPagamento;
    private String observacoes;
}
