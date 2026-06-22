package com.jotion.lp.Controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import com.jotion.lp.DTO.AuthResponseDTO;
import com.jotion.lp.DTO.LoginDTO;
import com.jotion.lp.DTO.UsuarioDTO;
import com.jotion.lp.Entity.Usuario;
import com.jotion.lp.Service.UsuarioService;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public List<UsuarioDTO> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public UsuarioDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public UsuarioDTO criar(@RequestBody Usuario usuario) {
        return service.salvar(usuario);
    }

    @PutMapping("/{id}")
    public UsuarioDTO atualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        return service.atualizar(id, usuario);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody LoginDTO dados) {
        return service.login(dados);
    }

    @PostMapping("/refresh")
    public AuthResponseDTO refresh(@RequestBody String refreshToken) {
        return service.refresh(refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody String refreshToken) {
        service.logout(refreshToken);
        return ResponseEntity.ok("Logout realizado com sucesso!");
    }

    @GetMapping("/me")
    public UsuarioDTO me(HttpServletRequest request) {
        String nome = (String) request.getAttribute("nomeUsuario");
        return service.buscarPorNome(nome);
    }
}