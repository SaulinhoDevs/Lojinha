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
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public void run(String... args) throws Exception {

        Usuario usuarioPF = new UsuarioPF(null, "Teste PF", "testepf@teste.com", "$2a$12$9mqmBztcEr/awMfTabweMuoubkYk8IgUleIwDZtSMqODUYk5r.QJS", "Teste PF", "Teste bairroPF", 123, "11999998888", "12345678901");
        Usuario usuarioPJ = new UsuarioPJ(null, "Teste PJ", "testepj@teste.com", "$2a$12$dxQXWcW2HbsENxL.X6t5deJj4sUFoiwi7Haep4xMUbvd6cBkD4i.W", "Teste PJ", "Teste bairroPJ", 456, "22888887777", "10987654321");

        usuarioRepository.saveAll(Arrays.asList(usuarioPF, usuarioPJ));

        Categoria cat1 = new Categoria(null, "Eletronicos", "Produtos Eletronicos", usuarioPF);
        Categoria cat2 = new Categoria(null, "Masculino", "Artigos Masculinos", usuarioPJ);
        Categoria cat3 = new Categoria(null, "Feminino", "Artigos Femininos", usuarioPF);
        Categoria cat4 = new Categoria(null, "Esportivo", "Artigos Esportivos", usuarioPJ);

        categoriaRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4));
    }
}