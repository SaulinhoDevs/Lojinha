package com.lojinhasystem.system.services;

import com.lojinhasystem.system.entities.Categoria;
import com.lojinhasystem.system.entities.Usuario;
import com.lojinhasystem.system.repositories.CategoriaRepository;
import com.lojinhasystem.system.resources.dto.CategoriaRequestDTO;
import com.lojinhasystem.system.resources.dto.CategoriaResponseDTO;
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

    public List<CategoriaResponseDTO> findAll() {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();
        return categoriaRepository.findByUsuarioId(usuarioLogado.getId())
                .stream()
                .map(CategoriaResponseDTO::new)
                .toList();
    }

    public CategoriaResponseDTO findById(Long id) {
        Categoria categoria = findEntityById(id);
        return new CategoriaResponseDTO(categoria);
    }

    public CategoriaResponseDTO insert(CategoriaRequestDTO dto) {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();

        Categoria categoria = new Categoria();
        categoria.setNome(dto.getNome());
        categoria.setDescricao(dto.getDescricao());
        categoria.setUsuario(usuarioLogado);

        categoria = categoriaRepository.save(categoria);
        return new CategoriaResponseDTO(categoria);
    }

    public CategoriaResponseDTO update(Long id, CategoriaRequestDTO dto) {
        Categoria entity = findEntityById(id);
        updateData(entity, dto);
        entity = categoriaRepository.save(entity);
        return new CategoriaResponseDTO(entity);
    }

    public void delete(Long id) {
        try {
            Categoria categoria = findEntityById(id);
            categoriaRepository.delete(categoria);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private void updateData(Categoria entity, CategoriaRequestDTO dto) {
        entity.setNome(dto.getNome());
        entity.setDescricao(dto.getDescricao());
    }

    private Categoria findEntityById(Long id) {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();

        return categoriaRepository.findByIdAndUsuarioId(id, usuarioLogado.getId())
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }
}