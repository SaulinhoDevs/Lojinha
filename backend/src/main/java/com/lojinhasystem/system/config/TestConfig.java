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

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Override
    public void run(String... args) throws Exception {

        // ---------- USUÁRIOS ----------
        Usuario usuarioPF = new UsuarioPF(null, "Teste PF", "testepf@teste.com", "$2a$12$hud7VrkO/lMHeP2AEmbB4..VTYzh/Q5/iGE7V45IY5uz1X9Me1jk6", "Teste PF", "Teste bairroPF", 123, "11999998888", "12345678901");
        Usuario usuarioPJ = new UsuarioPJ(null, "Teste PJ", "testepj@teste.com", "$2a$12$hud7VrkO/lMHeP2AEmbB4..VTYzh/Q5/iGE7V45IY5uz1X9Me1jk6", "Teste PJ", "Teste bairroPJ", 456, "22888887777", "10987654321");

        usuarioRepository.saveAll(Arrays.asList(usuarioPF, usuarioPJ));

        // ---------- CATEGORIAS ----------
        Categoria cat1 = new Categoria(null, "Eletronicos", "Produtos Eletronicos", usuarioPF);
        Categoria cat2 = new Categoria(null, "Masculino", "Artigos Masculinos", usuarioPJ);
        Categoria cat3 = new Categoria(null, "Feminino", "Artigos Femininos", usuarioPF);
        Categoria cat4 = new Categoria(null, "Esportivo", "Artigos Esportivos", usuarioPJ);

        categoriaRepository.saveAll(Arrays.asList(cat1, cat2, cat3, cat4));

        // ---------- PRODUTOS (usuarioPF) ----------
        Produto p1 = new Produto(null, "Fone Bluetooth", 50, 150.0, 80.0, usuarioPF);
        Produto p2 = new Produto(null, "Carregador USB-C", 3, 45.0, 20.0, usuarioPF);   // estoque baixo
        Produto p3 = new Produto(null, "Caixa de Som Portátil", 2, 220.0, 130.0, usuarioPF); // estoque baixo
        Produto p4 = new Produto(null, "Smartwatch", 15, 480.0, 300.0, usuarioPF);
        Produto p5 = new Produto(null, "Mouse Sem Fio", 40, 60.0, 30.0, usuarioPF);

        // ---------- PRODUTOS (usuarioPJ) ----------
        Produto p6 = new Produto(null, "Camisa Polo", 30, 90.0, 45.0, usuarioPJ);
        Produto p7 = new Produto(null, "Tênis Esportivo", 4, 250.0, 140.0, usuarioPJ);  // estoque baixo
        Produto p8 = new Produto(null, "Bermuda Jeans", 20, 120.0, 60.0, usuarioPJ);
        Produto p9 = new Produto(null, "Boné", 1, 40.0, 18.0, usuarioPJ);               // estoque baixo

        produtoRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9));

        p1.getCategorias().add(cat1);
        p2.getCategorias().add(cat1);
        p3.getCategorias().add(cat1);
        p4.getCategorias().add(cat1);
        p6.getCategorias().add(cat2);
        p7.getCategorias().add(cat4);
        p8.getCategorias().add(cat2);
        produtoRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p6, p7, p8));

        // ---------- CLIENTES (usuarioPF) ----------
        Cliente c1 = new Cliente(null, "João Silva", 0.0, "11911112222", "Rua A", "Centro", 10, usuarioPF);
        Cliente c2 = new Cliente(null, "Maria Souza", 150.0, "11922223333", "Rua B", "Centro", 20, usuarioPF); // com dívida

        // ---------- CLIENTES (usuarioPJ) ----------
        Cliente c3 = new Cliente(null, "Pedro Lima", 0.0, "22933334444", "Rua C", "Jardim", 30, usuarioPJ);
        Cliente c4 = new Cliente(null, "Ana Costa", 300.0, "22944445555", "Rua D", "Jardim", 40, usuarioPJ); // com dívida

        clienteRepository.saveAll(Arrays.asList(c1, c2, c3, c4));

        // ---------- VENDAS (usuarioPF) ----------
        LocalDate hoje = LocalDate.now();
        LocalDate dentroDoMes = hoje.withDayOfMonth(1).plusDays(2);
        LocalDate foraDoMes = hoje.minusMonths(2);

        Venda v1 = new Venda(null, dentroDoMes, 10.0, 5.0, StatusVenda.PAGO, c1, usuarioPF);
        Venda v2 = new Venda(null, dentroDoMes.plusDays(3), 0.0, 0.0, StatusVenda.PAGO, c2, usuarioPF);
        Venda v3 = new Venda(null, foraDoMes, 15.0, 0.0, StatusVenda.PAGO, c1, usuarioPF); // fora do período (não deve entrar no resumo do mês)

        // ---------- VENDAS (usuarioPJ) ----------
        Venda v4 = new Venda(null, dentroDoMes, 20.0, 10.0, StatusVenda.AGUARDANDO_PAGAMENTO, c3, usuarioPJ);
        Venda v5 = new Venda(null, dentroDoMes.plusDays(5), 0.0, 0.0, StatusVenda.PAGO, c4, usuarioPJ);

        vendaRepository.saveAll(Arrays.asList(v1, v2, v3, v4, v5));

        // ---------- ITENS (usuarioPF) ----------
        ItemPedido i1 = new ItemPedido(v1, p1, 2, p1.getPrecoVenda());   // 2x Fone Bluetooth
        ItemPedido i2 = new ItemPedido(v1, p5, 1, p5.getPrecoVenda());   // 1x Mouse Sem Fio
        ItemPedido i3 = new ItemPedido(v2, p4, 1, p4.getPrecoVenda());   // 1x Smartwatch
        ItemPedido i4 = new ItemPedido(v3, p2, 3, p2.getPrecoVenda());   // venda fora do mês

        // ---------- ITENS (usuarioPJ) ----------
        ItemPedido i5 = new ItemPedido(v4, p6, 4, p6.getPrecoVenda());   // 4x Camisa Polo
        ItemPedido i6 = new ItemPedido(v4, p8, 1, p8.getPrecoVenda());   // 1x Bermuda Jeans
        ItemPedido i7 = new ItemPedido(v5, p7, 2, p7.getPrecoVenda());   // 2x Tênis Esportivo

        itemPedidoRepository.saveAll(Arrays.asList(i1, i2, i3, i4, i5, i6, i7));
    }
}