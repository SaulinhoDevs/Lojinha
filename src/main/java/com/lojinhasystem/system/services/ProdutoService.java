package com.lojinhasystem.system.services;

import com.lojinhasystem.system.entities.Produto;
import com.lojinhasystem.system.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        return produto.get();
    }

    public Produto insert(Produto produto) {
        return produtoRepository.save(produto);
    }

    public void delete(Long id) {
        produtoRepository.deleteById(id);
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
    }

}