package com.lojinhasystem.system.services;

import com.lojinhasystem.system.entities.Produto;
import com.lojinhasystem.system.repositories.ProdutoRepository;
import com.lojinhasystem.system.services.exceptions.DatabaseException;
import com.lojinhasystem.system.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> findAll() {
        return produtoRepository.findAll();
    }

    public Produto findById(Long id) {
        Optional<Produto> produto = produtoRepository.findById(id);
        return produto.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Produto insert(Produto produto) {
        return produtoRepository.save(produto);
    }

    public void delete(Long id) {
        try {
            if (produtoRepository.existsById(id)) {
                produtoRepository.deleteById(id);
            } else {
                throw new ResourceNotFoundException(id);
            }
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public Produto update(Long id, Produto obj) {
        Produto produto = produtoRepository.getReferenceById(id);
        updateData(produto, obj);
        return produtoRepository.save(produto);
    }

    private void updateData(Produto produto, Produto obj) {
        produto.setNome(obj.getNome());
        produto.setEstoque(obj.getEstoque());
        produto.setPrecoVenda(obj.getPrecoVenda());
        produto.setPrecoCompra(obj.getPrecoCompra());
        produto.getCategorias().clear();
        produto.getCategorias().addAll(obj.getCategorias());
    }

}