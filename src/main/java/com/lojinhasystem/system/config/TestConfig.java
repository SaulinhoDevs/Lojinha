package com.lojinhasystem.system.config;

import com.lojinhasystem.system.entities.Produto;
import com.lojinhasystem.system.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private ProdutoRepository produtoRepository;


    @Override
    public void run(String... args) throws Exception {
        Produto p1 = new Produto(null, "Camisa Polo Masculina", 30, 79.90, 49.90);
        Produto p2 = new Produto(null, "Calça Jeans Feminina", 20, 129.90, 79.90);
        Produto p3 = new Produto(null, "Tênis Esportivo", 15, 199.90, 120.00);
        Produto p4 = new Produto(null, "Bolsa de Couro", 10, 249.90, 150.00);
        Produto p5 = new Produto(null, "Relógio Digital", 25, 149.90, 90.00);

        produtoRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5));
    }
}