package com.elitewheels.repositories;

import com.elitewheels.entities.Vendedor;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // Importe esta classe

public interface VendedorRepository extends JpaRepository<Vendedor, Long> {
    // Adicione este método para buscar um vendedor pelo nome
    Optional<Vendedor> findByNome(String nome);
}