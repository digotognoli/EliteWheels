package com.elitewheels.controllers;

import com.elitewheels.entities.Venda;
import com.elitewheels.entities.Vendedor;
import com.elitewheels.repositories.CarroRepository;
import com.elitewheels.repositories.VendaRepository;
import com.elitewheels.repositories.VendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VendaController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VendedorRepository vendedorRepo;
    
    @Autowired
    private CarroRepository carroRepo;
    
    @Autowired
    private VendaRepository vendaRepo;

    // Rota para a página de login
    @GetMapping("/")
    public String login() {
        return "login";
    }

    // Rota para a página de cadastro
    @GetMapping("/cadastro")
    public String cadastroForm() {
        return "cadastro";
    }

    // MÉTODO DE CADASTRO CORRIGIDO
    @PostMapping("/cadastrar")
    public String cadastrar(@RequestParam String nome, @RequestParam String senha) {
        
        // 1. Cria um objeto Vendedor novo e limpo
        Vendedor novoVendedor = new Vendedor();
        
        // 2. Define o nome
        novoVendedor.setNome(nome);
        
        // 3. Codifica a senha e a define no objeto
        String senhaCodificada = passwordEncoder.encode(senha);
        novoVendedor.setSenha(senhaCodificada);
        
        // 4. Salva o objeto novo, garantindo que a senha codificada será persistida
        vendedorRepo.save(novoVendedor);
        
        return "redirect:/";
    }

    // Rota para a página de orçamento (protegida)
    @GetMapping("/orcar")
    public String orcar(Model model) {
        model.addAttribute("carros", carroRepo.findAll());
        return "orcamento";
    }

    // Rota para salvar uma venda
    @PostMapping("/venda")
    public String salvarVenda(@ModelAttribute Venda venda) {
        vendaRepo.save(venda);
        return "redirect:/orcar";
    }
}