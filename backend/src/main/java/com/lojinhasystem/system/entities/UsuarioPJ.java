package com.lojinhasystem.system.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_usuario_pj")
@PrimaryKeyJoinColumn(name = "id")
public class UsuarioPJ extends Usuario {

    private String cnpj;

    public UsuarioPJ() {
    }

    public UsuarioPJ(Long id, String nome, String email, String senha, String rua, String bairro, Integer numero, String telefone, String cnpj) {
        super(id, nome, email, senha, rua, bairro, numero, telefone);
        this.cnpj = cnpj;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}