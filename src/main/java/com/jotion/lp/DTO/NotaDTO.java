package com.jotion.lp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotaDTO {
    private Long id;
    private String titulo;
    private String conteudo;
    private String nomeUsuario;
    private List<String> colaboradores;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
}
