package com.lojinhasystem.system.services;

import com.lojinhasystem.system.entities.Categoria;
import com.lojinhasystem.system.entities.Produto;
import com.lojinhasystem.system.entities.Usuario;
import com.lojinhasystem.system.repositories.CategoriaRepository;
import com.lojinhasystem.system.repositories.ProdutoRepository;
import com.lojinhasystem.system.resources.dto.ProdutoRequestDTO;
import com.lojinhasystem.system.resources.dto.ProdutoResponseDTO;
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

    public List<ProdutoResponseDTO> findAll() {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();
        return produtoRepository.findByUsuarioId(usuarioLogado.getId())
                .stream()
                .map(ProdutoResponseDTO::new)
                .toList();
    }

    public ProdutoResponseDTO findById(Long id) {
        Produto produto = findEntityById(id);
        return new ProdutoResponseDTO(produto);
    }

    public ProdutoResponseDTO insert(ProdutoRequestDTO dto) {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();

        Set<Categoria> categoriasValidadas = validarCategoriasDoUsuario(
                dto.getCategoriasIds(),
                usuarioLogado.getId()
        );

        Produto novoProduto = new Produto();
        novoProduto.setNome(dto.getNome());
        novoProduto.setEstoque(dto.getEstoque());
        novoProduto.setPrecoVenda(dto.getPrecoVenda());
        novoProduto.setPrecoCompra(dto.getPrecoCompra());
        novoProduto.setUsuario(usuarioLogado);
        novoProduto.getCategorias().addAll(categoriasValidadas);

        novoProduto = produtoRepository.save(novoProduto);
        return new ProdutoResponseDTO(novoProduto);
    }

    public ProdutoResponseDTO update(Long id, ProdutoRequestDTO dto) {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();

        Produto produto = findEntityById(id);
        updateData(produto, dto, usuarioLogado.getId());

        produto = produtoRepository.save(produto);
        return new ProdutoResponseDTO(produto);
    }

    public void delete(Long id) {
        try {
            Produto produto = findEntityById(id);
            produtoRepository.delete(produto);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private void updateData(Produto produto, ProdutoRequestDTO dto, Long usuarioId) {
        produto.setNome(dto.getNome());
        produto.setEstoque(dto.getEstoque());
        produto.setPrecoVenda(dto.getPrecoVenda());
        produto.setPrecoCompra(dto.getPrecoCompra());

        produto.getCategorias().clear();
        produto.getCategorias().addAll(validarCategoriasDoUsuario(dto.getCategoriasIds(), usuarioId));
    }

    private Set<Categoria> validarCategoriasDoUsuario(List<Long> categoriasIds, Long usuarioId) {
        Set<Categoria> categoriasValidadas = new HashSet<>();

        if (categoriasIds == null) {
            return categoriasValidadas;
        }

        for (Long categoriaId : categoriasIds) {
            Categoria categoriaValida = categoriaRepository
                    .findByIdAndUsuarioId(categoriaId, usuarioId)
                    .orElseThrow(() -> new ResourceNotFoundException(categoriaId));

            categoriasValidadas.add(categoriaValida);
        }

        return categoriasValidadas;
    }

    private Produto findEntityById(Long id) {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();

        return produtoRepository.findByIdAndUsuarioId(id, usuarioLogado.getId())
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }
}