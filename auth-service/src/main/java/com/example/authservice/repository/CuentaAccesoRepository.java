package com.example.authservice.repository;

import com.example.authservice.model.CuentaAcceso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CuentaAccesoRepository extends JpaRepository<CuentaAcceso, Long> {
    Optional<CuentaAcceso> findByEmail(String email);
    boolean existsByEmail(String email);
    List<CuentaAcceso> findByRolIgnoreCase(String rol);
    List<CuentaAcceso> findByEstadoIgnoreCase(String estado);
    List<CuentaAcceso> findByRolIgnoreCaseAndEstadoIgnoreCase(String rol, String estado);
}
