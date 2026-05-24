package com.example.userservice.repository;

import com.example.userservice.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByNickname(String nickname);
    Optional<Usuario> findByEmail(String email);
    boolean existsByNicknameIgnoreCase(String nickname);
    List<Usuario> findByRolIgnoreCase(String rol);
    List<Usuario> findByEstadoIgnoreCase(String estado);
}
