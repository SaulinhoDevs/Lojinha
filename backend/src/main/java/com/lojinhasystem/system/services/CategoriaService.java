package com.lojinhasystem.system.services;

import com.lojinhasystem.system.entities.Categoria;
import com.lojinhasystem.system.repositories.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Categoria findById(Long id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        return categoria.get();
    }

    public Categoria update(Long id, Categoria obj) {
        Categoria entity = categoriaRepository.getReferenceById(id);
        updateData(entity, obj);
        return categoriaRepository.save(entity);
    }

    private void updateData(Categoria entity, Categoria obj) {
        entity.setNome(obj.getNome());
        entity.setDescricao(obj.getDescricao());
    }

    public Categoria insert(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    public void delete(Long id) {
        categoriaRepository.deleteById(id);
    }
}