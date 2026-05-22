package com.authservice.service;

import com.authservice.model.CuentaAcceso;
import com.authservice.model.Estado;
import com.authservice.model.Rol;
import com.authservice.repository.CuentaAccesoRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CuentaAccesoService {

    private final CuentaAccesoRepository repository;
    private final BCryptPasswordEncoder passwordEncoder;

    public CuentaAccesoService(CuentaAccesoRepository repository) {
        this.repository = repository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }


    public CuentaAcceso crearCuenta(CuentaAcceso cuenta) {
        if (repository.existsByEmail(cuenta.getEmail())) {
            throw new IllegalArgumentException("El correo electrónico ya está registrado.");
        }
        cuenta.setPasswordHash(passwordEncoder.encode(cuenta.getPasswordHash()));
        return repository.save(cuenta);
    }

    public List<CuentaAcceso> listarCuentas(Rol rol, Estado estado) {
        if (rol != null && estado != null) return repository.findByRolAndEstado(rol, estado);
        if (rol != null) return repository.findByRol(rol);
        if (estado != null) return repository.findByEstado(estado);
        return repository.findAll();
    }

    public Optional<CuentaAcceso> buscarPorId(Long id) { return repository.findById(id); }
    public Optional<CuentaAcceso> buscarPorEmail(String email) { return repository.findByEmail(email); }

    // 4. Actualizar contraseña, rol o estado
    public CuentaAcceso actualizarCuenta(Long id, String nuevaPassword, Rol nuevoRol, Estado nuevoEstado) {
        CuentaAcceso cuenta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        if (nuevaPassword != null && !nuevaPassword.isBlank()) {
            cuenta.setPasswordHash(passwordEncoder.encode(nuevaPassword));
        }
        if (nuevoRol != null) cuenta.setRol(nuevoRol);
        if (nuevoEstado != null) cuenta.setEstado(nuevoEstado);

        return repository.save(cuenta);
    }

    public void desactivarCuenta(Long id) {
        CuentaAcceso cuenta = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
        cuenta.setEstado(Estado.DESACTIVADO);
        repository.save(cuenta);
    }

    public Optional<CuentaAcceso> verificarCredenciales(String email, String password Plain) {
        Optional<CuentaAcceso> cuentaOpt = repository.findByEmail(email);
        if (cuentaOpt.isPresent() && passwordEncoder.matches(passwordPlain, cuentaOpt.get().getPasswordHash())) {
            if (cuentaOpt.get().getEstado() == Estado.DESACTIVADO) {
                throw new RuntimeException("La cuenta está desactivada.");
            }
            return cuentaOpt;
        }
        return Optional.empty();
    }
}