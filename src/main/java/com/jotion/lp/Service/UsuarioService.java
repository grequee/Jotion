package com.jotion.lp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.jotion.lp.Entity.Usuario;
import com.jotion.lp.Repository.UsuarioRepository;
import java.util.List;

@Service
public class UsuarioService {
     @Autowired
    private UsuarioRepository repository;

    public List<Usuario> listarTodos() {
        return repository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public Usuario salvar(Usuario usuario) {
        return repository.save(usuario);
    }

    public Usuario atualizar(Long id, Usuario dadosNovos) {
        Usuario usuario = buscarPorId(id);
        usuario.setNome(dadosNovos.getNome());
        usuario.setSenha(dadosNovos.getSenha());
        return repository.save(usuario);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
