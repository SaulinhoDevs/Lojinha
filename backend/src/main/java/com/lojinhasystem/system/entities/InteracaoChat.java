package com.lojinhasystem.system.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_interacao_chat")
public class InteracaoChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT") // TEXT é importante pois respostas da IA costumam ser grandes
    private String pergunta;

    @Column(columnDefinition = "TEXT")
    private String resposta;

    private LocalDateTime dataHora;

    public InteracaoChat() {
    }

    public InteracaoChat(String pergunta, String resposta, LocalDateTime dataHora) {
        this.pergunta = pergunta;
        this.resposta = resposta;
        this.dataHora = dataHora;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
