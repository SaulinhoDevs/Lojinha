package com.lojinhasystem.system.services;

import com.lojinhasystem.system.entities.Categoria;
import com.lojinhasystem.system.entities.Usuario;
import com.lojinhasystem.system.repositories.CategoriaRepository;
import com.lojinhasystem.system.services.exceptions.DatabaseException;
import com.lojinhasystem.system.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    public List<Categoria> findAll() {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();
        return categoriaRepository.findByUsuarioId(usuarioLogado.getId());
    }

    public Categoria findById(Long id) {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();

        return categoriaRepository.findByIdAndUsuarioId(id, usuarioLogado.getId())
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Categoria insert(Categoria categoria) {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();
        categoria.setUsuario(usuarioLogado);
        return categoriaRepository.save(categoria);
    }

    public Categoria update(Long id, Categoria obj) {
        Categoria entity = findById(id);
        updateData(entity, obj);
        return categoriaRepository.save(entity);
    }

    private void updateData(Categoria entity, Categoria obj) {
        entity.setNome(obj.getNome());
        entity.setDescricao(obj.getDescricao());
    }

    public void delete(Long id) {
        try {
            Categoria categoria = findById(id);
            categoriaRepository.delete(categoria);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}