package com.jotion.lp.DTO;
import java.time.LocalDateTime;

public class UsuarioDTO {
     private Long id;
    private String nome;
    private LocalDateTime dataCriacao;

    public UsuarioDTO(Long id, String nome, LocalDateTime dataCriacao) {
        this.id = id;
        this.nome = nome;
        this.dataCriacao = dataCriacao;
}
public Long getId() { return id; }
    public String getNome() { return nome; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
}