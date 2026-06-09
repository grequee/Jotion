package com.jotion.lp.Repository;

import com.jotion.lp.Entity.Nota;
import com.jotion.lp.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotaRepository extends JpaRepository<Nota, Long> {
    @Query("SELECT DISTINCT n FROM Nota n LEFT JOIN n.colaboradores c WHERE n.dono = :usuario OR c = :usuario")
    List<Nota> findByDonoOrColaborador(@Param("usuario") Usuario usuario);
}
