package com.jotion.lp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErroResponseDTO {
    private int status;
    private String mensagem;
    private LocalDateTime timestamp;
}