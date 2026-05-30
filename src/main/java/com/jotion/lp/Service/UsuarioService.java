package com.jotion.lp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
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
        // Criptografa antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return toDTO(repository.save(usuario));
    }

    public UsuarioDTO atualizar(Long id, Usuario dadosNovos) {
        Usuario usuario = buscarEntidade(id);
        usuario.setNome(dadosNovos.getNome());
        // Criptografa antes de atualizar
        usuario.setSenha(passwordEncoder.encode(dadosNovos.getSenha()));
        return toDTO(repository.save(usuario));
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }

    private Usuario buscarEntidade(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}