package com.jotion.lp.Config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.jotion.lp.DTO.ErroResponseDTO;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponseDTO> handleRuntimeException(RuntimeException e) {
        String mensagem = e.getMessage();
        HttpStatus status;

        if (mensagem.contains("não encontrado")) {
            status = HttpStatus.NOT_FOUND; // 404
        } else if (mensagem.contains("inválido") || mensagem.contains("incorreta")) {
            status = HttpStatus.UNAUTHORIZED; // 401
        } else if (mensagem.contains("expirado")) {
            status = HttpStatus.UNAUTHORIZED; // 401
        } else {
            status = HttpStatus.BAD_REQUEST; // 400
        }

        ErroResponseDTO erro = new ErroResponseDTO(
            status.value(),
            mensagem,
            LocalDateTime.now()
        );

        return ResponseEntity.status(status).body(erro);
    }
}