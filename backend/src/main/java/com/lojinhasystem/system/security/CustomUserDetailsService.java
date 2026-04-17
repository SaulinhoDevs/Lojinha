package com.lojinhasystem.system.security;

import com.lojinhasystem.system.entities.Usuario;
import com.lojinhasystem.system.entities.UsuarioPF;
import com.lojinhasystem.system.entities.UsuarioPJ;
import com.lojinhasystem.system.repositories.UsuarioPFRepository;
import com.lojinhasystem.system.repositories.UsuarioPJRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioPFRepository usuarioPFRepository;

    @Autowired
    private UsuarioPJRepository usuarioPJRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UsuarioPF> usuarioPF = usuarioPFRepository.findByEmail(email);
        if (usuarioPF.isPresent()) {
            return new UsuarioAutenticado(usuarioPF.get());
        }

        Optional<UsuarioPJ> usuarioPJ = usuarioPJRepository.findByEmail(email);
        if (usuarioPJ.isPresent()) {
            return new UsuarioAutenticado(usuarioPJ.get());
        }

        throw new UsernameNotFoundException("Usuário não encontrado");
    }

    public Usuario buscarUsuarioPorEmail(String email) {
        Optional<UsuarioPF> usuarioPF = usuarioPFRepository.findByEmail(email);
        if (usuarioPF.isPresent()) {
            return usuarioPF.get();
        }

        Optional<UsuarioPJ> usuarioPJ = usuarioPJRepository.findByEmail(email);
        if (usuarioPJ.isPresent()) {
            return usuarioPJ.get();
        }

        throw new UsernameNotFoundException("Usuário não encontrado");
    }
}
