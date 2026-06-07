package com.jotion.lp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jotion.lp.Entity.RefreshToken;
import com.jotion.lp.Entity.Usuario;
import com.jotion.lp.Repository.RefreshTokenRepository;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Autowired
    private RefreshTokenRepository repository;

    @Transactional // ← adicionado aqui
    public RefreshToken criar(Usuario usuario) {
        repository.deleteByUsuario(usuario);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUsuario(usuario);
        refreshToken.setExpiracao(LocalDateTime.now().plusDays(7));

        return repository.save(refreshToken);
    }

    public RefreshToken validar(String token) {
        RefreshToken refreshToken = repository.findByToken(token)
            .orElseThrow(() -> new RuntimeException("Refresh token inválido"));

        if (refreshToken.getExpiracao().isBefore(LocalDateTime.now())) {
            repository.delete(refreshToken);
            throw new RuntimeException("Refresh token expirado, faça login novamente");
        }

        return refreshToken;
    }
}