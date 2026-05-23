package com.example.authservice.repository;

import com.example.authservice.model.CuentaAcceso;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public interface CuentaAccesoRepository {
    CuentaAcceso save(CuentaAcceso cuenta);
    List<CuentaAcceso> findAll();
    Optional<CuentaAcceso> findById(Long id);
    Optional<CuentaAcceso> findByEmail(String email);
    boolean existsByEmail(String email);
}

@Repository
class InMemoryCuentaAccesoRepository implements CuentaAccesoRepository {
    private final ConcurrentHashMap<Long, CuentaAcceso> data = new ConcurrentHashMap<>();
    private final AtomicLong ids = new AtomicLong(1);

    public CuentaAcceso save(CuentaAcceso cuenta) {
        if (cuenta.getId() == null) cuenta.setId(ids.getAndIncrement());
        data.put(cuenta.getId(), cuenta);
        return cuenta;
    }

    public List<CuentaAcceso> findAll() {
        return data.values().stream().sorted(Comparator.comparing(CuentaAcceso::getId)).toList();
    }

    public Optional<CuentaAcceso> findById(Long id) {
        return Optional.ofNullable(data.get(id));
    }

    public Optional<CuentaAcceso> findByEmail(String email) {
        return data.values().stream().filter(c -> c.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    public boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }
}
