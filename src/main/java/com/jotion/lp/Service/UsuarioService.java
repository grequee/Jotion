package com.jotion.lp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.jotion.lp.Config.JwtUtil;
import com.jotion.lp.DTO.LoginDTO;
import com.jotion.lp.DTO.UsuarioDTO;
import com.jotion.lp.Entity.Usuario;
import com.jotion.lp.Repository.UsuarioRepository;
import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;  // ← adicionado aqui

    private UsuarioDTO toDTO(Usuario u) {
        return new UsuarioDTO(u.getId(), u.getNome(), u.getDataCriacao());
    }

    public List<UsuarioDTO> listarTodos() {
        return repository.findAll()
            .stream()
            .map(this::toDTO)
            .toList();
    }

    public UsuarioDTO buscarPorId(Long id) {
        return toDTO(buscarEntidade(id));
    }

    public UsuarioDTO salvar(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return toDTO(repository.save(usuario));
    }

    public UsuarioDTO atualizar(Long id, Usuario dadosNovos) {
        Usuario usuario = buscarEntidade(id);
        usuario.setNome(dadosNovos.getNome());
        usuario.setSenha(passwordEncoder.encode(dadosNovos.getSenha()));
        return toDTO(repository.save(usuario));
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    public String login(LoginDTO dados) {  // ← adicionado aqui
        Usuario usuario = repository.findByNome(dados.getNome())
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (passwordEncoder.matches(dados.getSenha(), usuario.getSenha())) {
            return jwtUtil.gerarToken(usuario.getNome());
        } else {
            throw new RuntimeException("Senha incorreta");
        }
    }

    private Usuario buscarEntidade(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}