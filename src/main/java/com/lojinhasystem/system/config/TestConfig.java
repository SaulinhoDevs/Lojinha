package com.lojinhasystem.system.config;

import com.lojinhasystem.system.entities.*;
import com.lojinhasystem.system.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.Arrays;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioPFRepository usuarioPFRepository;

    @Autowired
    private UsuarioPJRepository usuarioPJRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @Override
    public void run(String... args) throws Exception {
        Produto p1 = new Produto(null, "Camisa Polo Masculina", 30, 79.90, 49.90);
        Produto p2 = new Produto(null, "Calça Jeans Feminina", 20, 129.90, 79.90);
        Produto p3 = new Produto(null, "Tênis Esportivo", 15, 199.90, 120.00);
        Produto p4 = new Produto(null, "Bolsa de Couro", 10, 249.90, 150.00);
        Produto p5 = new Produto(null, "Relógio Digital", 25, 149.90, 90.00);

        produtoRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5));

        // Pessoas Físicas
        UsuarioPF u1 = new UsuarioPF(null, "Mariana Souza", "Rua das Flores", "Centro", 123, "(75) 98877-1122", "123.456.789-00");
        UsuarioPF u2 = new UsuarioPF(null, "Carlos Lima", "Av. Principal", "Jardim América", 456, "(75) 98123-4455", "987.654.321-00");
        UsuarioPF u3 = new UsuarioPF(null, "Fernanda Alves", "Rua do Sol", "Serrano", 78, "(75) 98765-0987", "321.654.987-00");

        // Pessoas Jurídicas
        UsuarioPJ u4 = new UsuarioPJ(null, "Loja da Ana LTDA", "Av. Getúlio Vargas", "Centro", 1001, "(75) 99234-5566", "12.345.678/0001-99");
        UsuarioPJ u5 = new UsuarioPJ(null, "Mercadinho São José", "Rua das Palmeiras", "São Pedro", 220, "(75) 98700-1122", "98.765.432/0001-55");

        usuarioPFRepository.saveAll(Arrays.asList(u1, u2, u3));
        usuarioPJRepository.saveAll(Arrays.asList(u4, u5));

        Cliente c1 = new Cliente(null, "Maria Oliveira", 0.0, "71988887777", "Rua das Flores", "Centro", 101);
        Cliente c2 = new Cliente(null, "João Santos", 50.0, "71999996666", "Av. Brasil", "São José", 202);
        Cliente c3 = new Cliente(null, "Ana Costa", 120.0, "71997778888", "Rua da Paz", "Liberdade", 303);
        Cliente c4 = new Cliente(null, "Carlos Souza", 0.0, "71995554444", "Rua Bahia", "Cajazeiras", 404);
        Cliente c5 = new Cliente(null, "Fernanda Lima", 200.0, "71992221111", "Rua Verde", "Brotas", 505);

        Venda v1 = new Venda(null, 350.00, LocalDate.of(2025, 10, 10), 20.00, 10.00, c1);
        Venda v2 = new Venda(null, 180.00, LocalDate.of(2025, 10, 11), 0.00, 0.00, c2);
        Venda v3 = new Venda(null, 950.00, LocalDate.of(2025, 10, 12), 35.00, 50.00, c3);
        Venda v4 = new Venda(null, 120.00, LocalDate.of(2025, 10, 15), 15.00, 0.00, c4);
        Venda v5 = new Venda(null, 765.00, LocalDate.of(2025, 10, 20), 0.00, 30.00, c5);
        Venda v6 = new Venda(null, 100.00, LocalDate.of(2024, 4, 20), 30.00, 15.00, c1);

        clienteRepository.saveAll(Arrays.asList(c1, c2, c3, c4, c5));
        vendaRepository.saveAll(Arrays.asList(v1, v2, v3, v4, v5, v6));
    }
}