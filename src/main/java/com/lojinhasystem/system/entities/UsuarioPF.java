package com.lojinhasystem.system.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_usuario_pf")
public class UsuarioPF extends Usuario {

    private String cpf;

    public UsuarioPF(Long id, String nome, String rua, String bairro, Integer numero, String telefone, String cpf) {
        super(id, nome, rua, bairro, numero, telefone);
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}