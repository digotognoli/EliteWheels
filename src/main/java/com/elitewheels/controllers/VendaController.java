package com.elitewheels.controllers;

import com.elitewheels.entities.Carro;
import com.elitewheels.entities.Comprador;
import com.elitewheels.entities.Venda;
import com.elitewheels.entities.Vendedor;
import com.elitewheels.repositories.CarroRepository;
import com.elitewheels.repositories.CompradorRepository;
import com.elitewheels.repositories.VendaRepository;
import com.elitewheels.repositories.VendedorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class VendaController {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private VendedorRepository vendedorRepo;
    @Autowired
    private CarroRepository carroRepo;
    @Autowired
    private VendaRepository vendaRepo; // Será usado no método finalizarVenda
    @Autowired
    private CompradorRepository compradorRepo; // Será usado no método prepararVenda

    // Redireciona a raiz (/) para o login ou para a página principal
    @GetMapping("/")
    public String handleRoot(Principal principal) {
        if (principal != null) {
            return "redirect:/orcar";
        }
        return "redirect:/login";
    }

    // Mapeia a página de login
    @GetMapping("/login")
    public String loginPage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            return "redirect:/orcar";
        }
        return "login";
    }

    // Mapeia o formulário de cadastro
    @GetMapping("/cadastro")
    public String cadastroForm() {
        return "cadastro";
    }

    // Processa o cadastro de um novo vendedor
    @PostMapping("/cadastrar")
    public String cadastrar(@RequestParam String nome, @RequestParam String senha) {
        Vendedor novoVendedor = new Vendedor();
        novoVendedor.setNome(nome);
        novoVendedor.setSenha(passwordEncoder.encode(senha));
        vendedorRepo.save(novoVendedor);
        return "redirect:/login";
    }

    // Mapeia a página de orçamento
    @GetMapping("/orcar")
    public String orcar(Model model) {
        model.addAttribute("carros", carroRepo.findAll());
        return "orcamento";
    }

    // Prepara os dados para a tela final de venda
    @PostMapping("/venda/preparar")
    @Transactional
    public String prepararVenda(
            @RequestParam("nome") String compradorNome,
            @RequestParam("cpf") String compradorCpf,
            @RequestParam("dataNascimento") String compradorDataNascimento,
            @RequestParam Long carroCodigo,
            @RequestParam double valorVeiculo,
            Model model) {

        // O 'compradorRepo' é usado aqui, o que remove o primeiro aviso.
        Comprador novoComprador = new Comprador();
        novoComprador.setNome(compradorNome);
        novoComprador.setCpf(compradorCpf);
        novoComprador.setDataNascimento(compradorDataNascimento);
        Comprador compradorSalvo = compradorRepo.save(novoComprador);

        Carro carroSelecionado = carroRepo.findById(carroCodigo)
                .orElseThrow(() -> new IllegalArgumentException("Código do carro inválido: " + carroCodigo));

        Venda venda = new Venda();
        venda.setComprador(compradorSalvo);
        venda.setCarro(carroSelecionado);
        venda.setValorFinal(valorVeiculo);
        venda.setDataHora(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        model.addAttribute("venda", venda);
        
        return "venda";
    }

    // Finaliza a venda e salva no banco de dados
    @PostMapping("/venda/finalizar")
    @Transactional
    public String finalizarVenda(@ModelAttribute Venda venda, Authentication authentication, RedirectAttributes redirectAttributes) {
        String nomeVendedor = authentication.getName();
        Vendedor vendedorLogado = vendedorRepo.findByNome(nomeVendedor)
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));

        venda.setVendedor(vendedorLogado);
        venda.setDataHora(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        // O 'vendaRepo' é usado aqui, o que remove o segundo aviso.
        vendaRepo.save(venda);
        
        redirectAttributes.addFlashAttribute("successMessage", "Venda efetuada com sucesso!");
        
        return "redirect:/orcar";
    }
}