package com.elitewheels.config;

import com.elitewheels.entities.Vendedor;
import com.elitewheels.repositories.VendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class VendedorDetailsService implements UserDetailsService {

    @Autowired
    private VendedorRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // O "username" agora é o NOME do vendedor, não mais o ID.
        Vendedor vendedor = repository.findByNome(username)
            .orElseThrow(() -> new UsernameNotFoundException("Vendedor não encontrado com o nome: " + username));

        return User.builder()
            .username(vendedor.getNome()) // Usamos o nome aqui
            .password(vendedor.getSenha())
            .roles("VENDEDOR")
            .build();
    }
}   