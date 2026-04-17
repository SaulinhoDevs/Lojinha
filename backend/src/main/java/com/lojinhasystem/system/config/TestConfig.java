package com.lojinhasystem.system.config;

import com.lojinhasystem.system.entities.*;
import com.lojinhasystem.system.entities.enums.StatusVenda;
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

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Override
    public void run(String... args) throws Exception {

        Categoria cat1 = new Categoria(null, "Eletronicos", "Produtos Eletronicos");
        Categoria cat2 = new Categoria(null, "Masculino", "Artigos Masculinos");
        Categoria cat3 = new Categoria(null, "Feminino", "Artigos Femininos");
        Categoria cat4 = new Categoria(null, "Esportivo", "Artigos Esportivos");

        Produto p1 = new Produto(null, "Camisa Polo Masculina", 30, 79.90, 49.90);
        Produto p2 = new Produto(null, "Calça Jeans Feminina", 20, 129.90, 79.90);
        Produto p3 = new Produto(null, "Tênis Esportivo", 15, 199.90, 120.00);
        Produto p4 = new Produto(null, "Bolsa de Couro", 10, 249.90, 150.00);
        Produto p5 = new Produto(null, "Relógio Digital", 25, 149.90, 90.00);

        categoriaRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4));
        produtoRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5));

        p1.getCategorias().add(cat2);
        p2.getCategorias().add(cat3);
        p3.getCategorias().add(cat4);
        p4.getCategorias().add(cat3);
        p5.getCategorias().add(cat1);
        p5.getCategorias().add(cat2);

        produtoRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5));

        UsuarioPF usuarioPF = new UsuarioPF(null, "Somente Teste", "teste@teste.com", "$2a$12$9mqmBztcEr/awMfTabweMuoubkYk8IgUleIwDZtSMqODUYk5r.QJS", "Teste Rua", "Teste bairro", 123, "11999998888", "12345678901");

        usuarioPFRepository.saveAll(Arrays.asList(usuarioPF));

        Cliente c1 = new Cliente(null, "Maria Oliveira", 0.0, "71988887777", "Rua das Flores", "Centro", 101);
        Cliente c2 = new Cliente(null, "João Santos", 0.0, "71999996666", "Av. Brasil", "São José", 202);
        Cliente c3 = new Cliente(null, "Ana Costa", 0.0, "71997778888", "Rua da Paz", "Liberdade", 303);
        Cliente c4 = new Cliente(null, "Carlos Souza", 0.0, "71995554444", "Rua Bahia", "Cajazeiras", 404);
        Cliente c5 = new Cliente(null, "Fernanda Lima", 0.0, "71992221111", "Rua Verde", "Brotas", 505);

        clienteRepository.saveAll(Arrays.asList(c1, c2, c3, c4, c5));
    }
}