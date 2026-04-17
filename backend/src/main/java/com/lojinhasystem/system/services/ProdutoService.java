package com.lojinhasystem.system.services;

import com.lojinhasystem.system.entities.Categoria;
import com.lojinhasystem.system.entities.Produto;
import com.lojinhasystem.system.entities.Usuario;
import com.lojinhasystem.system.repositories.CategoriaRepository;
import com.lojinhasystem.system.repositories.ProdutoRepository;
import com.lojinhasystem.system.services.exceptions.DatabaseException;
import com.lojinhasystem.system.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    public List<Produto> findAll() {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();
        return produtoRepository.findByUsuarioId(usuarioLogado.getId());
    }

    public Produto findById(Long id) {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();

        return produtoRepository.findByIdAndUsuarioId(id, usuarioLogado.getId())
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Produto insert(Produto produto) {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();

        Set<Categoria> categoriasValidadas = validarCategoriasDoUsuario(
                produto.getCategorias(),
                usuarioLogado.getId()
        );

        Produto novoProduto = new Produto();
        novoProduto.setNome(produto.getNome());
        novoProduto.setEstoque(produto.getEstoque());
        novoProduto.setPrecoVenda(produto.getPrecoVenda());
        novoProduto.setPrecoCompra(produto.getPrecoCompra());
        novoProduto.setUsuario(usuarioLogado);
        novoProduto.getCategorias().addAll(categoriasValidadas);

        return produtoRepository.save(novoProduto);
    }

    public void delete(Long id) {
        try {
            Produto produto = findById(id);
            produtoRepository.delete(produto);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public Produto update(Long id, Produto obj) {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();

        Produto produto = findById(id);
        updateData(produto, obj, usuarioLogado.getId());

        return produtoRepository.save(produto);
    }

    private void updateData(Produto produto, Produto obj, Long usuarioId) {
        produto.setNome(obj.getNome());
        produto.setEstoque(obj.getEstoque());
        produto.setPrecoVenda(obj.getPrecoVenda());
        produto.setPrecoCompra(obj.getPrecoCompra());

        produto.getCategorias().clear();
        produto.getCategorias().addAll(validarCategoriasDoUsuario(obj.getCategorias(), usuarioId));
    }

    private Set<Categoria> validarCategoriasDoUsuario(Set<Categoria> categorias, Long usuarioId) {
        Set<Categoria> categoriasValidadas = new HashSet<>();

        for (Categoria categoria : categorias) {
            Categoria categoriaValida = categoriaRepository
                    .findByIdAndUsuarioId(categoria.getId(), usuarioId)
                    .orElseThrow(() -> new ResourceNotFoundException(categoria.getId()));

            categoriasValidadas.add(categoriaValida);
        }

        return categoriasValidadas;
    }
}