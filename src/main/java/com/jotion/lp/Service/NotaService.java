package com.jotion.lp.Service;

import com.jotion.lp.DTO.CriarNotaDTO;
import com.jotion.lp.DTO.NotaDTO;
import com.jotion.lp.Entity.Nota;
import com.jotion.lp.Entity.Usuario;
import com.jotion.lp.Repository.NotaRepository;
import com.jotion.lp.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotaService {

    @Autowired
    private NotaRepository notaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private NotaDTO toDTO(Nota nota) {
        NotaDTO dto = new NotaDTO();
        dto.setId(nota.getId());
        dto.setTitulo(nota.getTitulo());
        dto.setConteudo(nota.getConteudo());
        dto.setNomeUsuario(nota.getDono().getNome());
        dto.setColaboradores(nota.getColaboradores().stream().map(Usuario::getNome).collect(Collectors.toList()));
        dto.setDataCriacao(nota.getDataCriacao());
        dto.setDataAtualizacao(nota.getDataAtualizacao());
        return dto;
    }

    private Usuario getUsuario(String nome) {
        return usuarioRepository.findByNome(nome)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public List<NotaDTO> listar(String nomeUsuario) {
        Usuario usuario = getUsuario(nomeUsuario);
        return notaRepository.findByDonoOrColaborador(usuario).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public Nota buscarEntidadePorId(Long id, String nomeUsuario) {
        Nota nota = notaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nota não encontrada"));
        Usuario usuario = getUsuario(nomeUsuario);

        if (!nota.getDono().equals(usuario) && !nota.getColaboradores().contains(usuario)) {
            throw new RuntimeException("Sem permissão para acessar esta nota");
        }
        return nota;
    }

    public NotaDTO buscarPorId(Long id, String nomeUsuario) {
        return toDTO(buscarEntidadePorId(id, nomeUsuario));
    }

    public NotaDTO criar(CriarNotaDTO dto, String nomeUsuario) {
        Usuario dono = getUsuario(nomeUsuario);
        Nota nota = new Nota();
        nota.setTitulo(dto.getTitulo());
        nota.setConteudo(dto.getConteudo());
        nota.setDono(dono);
        return toDTO(notaRepository.save(nota));
    }

    public NotaDTO atualizar(Long id, CriarNotaDTO dto, String nomeUsuario) {
        Nota nota = buscarEntidadePorId(id, nomeUsuario);
        nota.setTitulo(dto.getTitulo());
        nota.setConteudo(dto.getConteudo());
        return toDTO(notaRepository.save(nota));
    }

    public void deletar(Long id, String nomeUsuario) {
        Nota nota = notaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nota não encontrada"));
        Usuario usuario = getUsuario(nomeUsuario);

        if (!nota.getDono().equals(usuario)) {
            throw new RuntimeException("Somente o dono pode deletar a nota");
        }
        notaRepository.delete(nota);
    }

    public NotaDTO adicionarColaborador(Long notaId, String nomeColaborador, String nomeUsuario) {
        Nota nota = notaRepository.findById(notaId)
                .orElseThrow(() -> new RuntimeException("Nota não encontrada"));
        Usuario dono = getUsuario(nomeUsuario);

        if (!nota.getDono().equals(dono)) {
            throw new RuntimeException("Somente o dono pode adicionar colaboradores");
        }

        Usuario colaborador = getUsuario(nomeColaborador);
        if (nota.getDono().equals(colaborador)) {
            throw new RuntimeException("O dono não pode ser adicionado como colaborador");
        }

        if (!nota.getColaboradores().contains(colaborador)) {
            nota.getColaboradores().add(colaborador);
            notaRepository.save(nota);
        }

        return toDTO(nota);
    }

    public void removerColaborador(Long notaId, String nomeColaborador, String nomeUsuario) {
        Nota nota = notaRepository.findById(notaId)
                .orElseThrow(() -> new RuntimeException("Nota não encontrada"));
        Usuario dono = getUsuario(nomeUsuario);

        if (!nota.getDono().equals(dono)) {
            throw new RuntimeException("Somente o dono pode remover colaboradores");
        }

        Usuario colaborador = getUsuario(nomeColaborador);
        if (nota.getColaboradores().contains(colaborador)) {
            nota.getColaboradores().remove(colaborador);
            notaRepository.save(nota);
        }
    }
}
