package com.lojinhasystem.system.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_usuario_pf")
@PrimaryKeyJoinColumn(name = "id")
public class UsuarioPF extends Usuario {

    private String cpf;

    public UsuarioPF() {
    }

    public UsuarioPF(Long id, String nome, String email, String senha, String rua, String bairro, Integer numero, String telefone, String cpf) {
        super(id, nome, email, senha, rua, bairro, numero, telefone);
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}