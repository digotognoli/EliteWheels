package com.elitewheels;

import com.elitewheels.entities.Carro;
import com.elitewheels.repositories.CarroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class EliteWheelsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EliteWheelsApplication.class, args);
    }

    // Injeta o repositório de carros para que possamos usá-lo
    @Autowired
    private CarroRepository carroRepository;

    // Este Bean será executado assim que a aplicação iniciar
    @Bean
    public CommandLineRunner init() {
        return args -> {
            
            // Cria os objetos Carro
            Carro c1 = new Carro(101L, "Toyota Corolla", "Prata", 2023, 145000.00);
            Carro c2 = new Carro(102L, "Honda Civic", "Preto", 2022, 155000.00);
            Carro c3 = new Carro(103L, "Hyundai Creta", "Branco", 2023, 135000.00);
            Carro c4 = new Carro(104L, "Jeep Renegade", "Vermelho", 2021, 120000.00);
            Carro c5 = new Carro(105L, "Chevrolet Onix", "Azul", 2024, 85000.00);

            // Salva a lista de carros no banco de dados
            carroRepository.saveAll(Arrays.asList(c1, c2, c3, c4, c5));
        };
    }
}