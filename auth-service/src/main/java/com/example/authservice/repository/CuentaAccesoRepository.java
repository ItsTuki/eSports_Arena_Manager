package com.authservice.repository;

import com.authservice.model.CuentaAcceso;
import com.authservice.model.Estado;
import com.authservice.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CuentaAccesoRepository extends JpaRepository<CuentaAcceso, Long> {
    Optional<CuentaAcceso> findByEmail(String email);
    List<CuentaAcceso> findByRol(Rol rol);
    List<CuentaAcceso> findByEstado(Estado estado);
    List<CuentaAcceso> findByRolAndEstado(Rol rol, Estado estado);
    boolean existsByEmail(String email);
}