package com.lojinhasystem.system.services;

import com.lojinhasystem.system.entities.Usuario;
import com.lojinhasystem.system.resources.dto.LoginRequest;
import com.lojinhasystem.system.resources.dto.LoginResponse;
import com.lojinhasystem.system.security.CustomUserDetailsService;
import com.lojinhasystem.system.security.JwtService;
import com.lojinhasystem.system.security.UsuarioAutenticado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getSenha()
                    )
            );

            Usuario usuario = customUserDetailsService.buscarUsuarioPorEmail(request.getEmail());
            UsuarioAutenticado userDetails = new UsuarioAutenticado(usuario);
            String token = jwtService.generateToken(userDetails);

            return new LoginResponse(token, usuario.getNome());

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Email ou senha inválidos");
        }
    }
}