package com.lojinhasystem.system.config;

import com.lojinhasystem.system.entities.Produto;
import com.lojinhasystem.system.entities.UsuarioPF;
import com.lojinhasystem.system.entities.UsuarioPJ;
import com.lojinhasystem.system.repositories.ProdutoRepository;
import com.lojinhasystem.system.repositories.UsuarioPFRepository;
import com.lojinhasystem.system.repositories.UsuarioPJRepository;
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

    @Autowired
    private UsuarioPFRepository usuarioPFRepository;

    @Autowired
    private UsuarioPJRepository usuarioPJRepository;

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
    }
}