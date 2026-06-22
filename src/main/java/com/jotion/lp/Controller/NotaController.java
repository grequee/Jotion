package com.jotion.lp.Controller;

import com.jotion.lp.DTO.AdicionarColaboradorDTO;
import com.jotion.lp.DTO.CriarNotaDTO;
import com.jotion.lp.DTO.NotaDTO;
import com.jotion.lp.Service.NotaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notas")
@CrossOrigin(origins = "*")
public class NotaController {

    @Autowired
    private NotaService notaService;

    private String getUsuarioLogado(HttpServletRequest request) {
        return (String) request.getAttribute("nomeUsuario");
    }

    @GetMapping
    public List<NotaDTO> listar(HttpServletRequest request) {
        return notaService.listar(getUsuarioLogado(request));
    }

    @GetMapping("/{id}")
    public NotaDTO buscarPorId(@PathVariable Long id, HttpServletRequest request) {
        return notaService.buscarPorId(id, getUsuarioLogado(request));
    }

    @PostMapping
    public NotaDTO criar(@RequestBody CriarNotaDTO dto, HttpServletRequest request) {
        return notaService.criar(dto, getUsuarioLogado(request));
    }

    @PutMapping("/{id}")
    public NotaDTO atualizar(@PathVariable Long id, @RequestBody CriarNotaDTO dto, HttpServletRequest request) {
        return notaService.atualizar(id, dto, getUsuarioLogado(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id, HttpServletRequest request) {
        notaService.deletar(id, getUsuarioLogado(request));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/colaboradores")
    public NotaDTO adicionarColaborador(@PathVariable Long id, @RequestBody AdicionarColaboradorDTO dto, HttpServletRequest request) {
        return notaService.adicionarColaborador(id, dto.getNomeColaborador(), getUsuarioLogado(request));
    }

    @DeleteMapping("/{id}/colaboradores/{nomeColaborador}")
    public ResponseEntity<Void> removerColaborador(@PathVariable Long id, @PathVariable String nomeColaborador, HttpServletRequest request) {
        notaService.removerColaborador(id, nomeColaborador, getUsuarioLogado(request));
        return ResponseEntity.noContent().build();
    }
}
