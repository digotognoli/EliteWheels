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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private VendaRepository vendaRepo;
    @Autowired
    private CompradorRepository compradorRepo;

    // ... (métodos de login, cadastro e orcar permanecem iguais) ...
    @GetMapping("/")
    public String login() {
        return "login";
    }

    @GetMapping("/cadastro")
    public String cadastroForm() {
        return "cadastro";
    }

    @PostMapping("/cadastrar")
    public String cadastrar(@RequestParam String nome, @RequestParam String senha) {
        Vendedor novoVendedor = new Vendedor();
        novoVendedor.setNome(nome);
        String senhaCodificada = passwordEncoder.encode(senha);
        novoVendedor.setSenha(senhaCodificada);
        vendedorRepo.save(novoVendedor);
        return "redirect:/";
    }

    @GetMapping("/orcar")
    public String orcar(Model model) {
        model.addAttribute("carros", carroRepo.findAll());
        return "orcamento";
    }

    // MÉTODO ATUALIZADO: Prepara a venda de forma explícita
    @PostMapping("/venda/preparar")
    @Transactional
    public String prepararVenda(
            @RequestParam("nome") String compradorNome,
            @RequestParam("cpf") String compradorCpf,
            @RequestParam("dataNascimento") String compradorDataNascimento,
            @RequestParam Long carroCodigo,
            @RequestParam double valorVeiculo,
            Model model) {

        // 1. Cria um novo objeto Comprador manualmente
        Comprador novoComprador = new Comprador();
        novoComprador.setNome(compradorNome);
        novoComprador.setCpf(compradorCpf);
        novoComprador.setDataNascimento(compradorDataNascimento);
        // 2. Salva o comprador para garantir que ele tenha um ID
        Comprador compradorSalvo = compradorRepo.save(novoComprador);

        // 3. Busca o carro selecionado
        Carro carroSelecionado = carroRepo.findById(carroCodigo)
                .orElseThrow(() -> new IllegalArgumentException("Código do carro inválido:" + carroCodigo));

        // 4. Monta o objeto de Venda com os dados corretos
        Venda venda = new Venda();
        venda.setComprador(compradorSalvo); // Usa o comprador que acabamos de criar e salvar
        venda.setCarro(carroSelecionado);
        venda.setValorFinal(valorVeiculo);
        venda.setDataHora(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        model.addAttribute("venda", venda);
        
        return "venda";
    }

    // O método finalizar agora receberá o ID do comprador salvo e funcionará corretamente
    @PostMapping("/venda/finalizar")
    public String finalizarVenda(@ModelAttribute Venda venda, Authentication authentication, RedirectAttributes redirectAttributes) {
        String nomeVendedor = authentication.getName();
        Vendedor vendedorLogado = vendedorRepo.findByNome(nomeVendedor)
                .orElseThrow(() -> new RuntimeException("Vendedor não encontrado"));

        venda.setVendedor(vendedorLogado);
        venda.setDataHora(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

        vendaRepo.save(venda);
        
        redirectAttributes.addFlashAttribute("successMessage", "Venda efetuada com sucesso!");
        
        return "redirect:/orcar";
    }
}