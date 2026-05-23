package com.example.gameservice.repository;

import com.example.gameservice.model.Juego;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public interface JuegoRepository {
    Juego save(Juego juego);
    List<Juego> findAll();
    Optional<Juego> findById(Long id);
    Optional<Juego> findByNombre(String nombre);
}

@Repository
class InMemoryJuegoRepository implements JuegoRepository {
    private final ConcurrentHashMap<Long, Juego> data = new ConcurrentHashMap<>();
    private final AtomicLong ids = new AtomicLong(1);

    public Juego save(Juego juego) {
        if (juego.getId() == null) juego.setId(ids.getAndIncrement());
        data.put(juego.getId(), juego);
        return juego;
    }

    public List<Juego> findAll() { return data.values().stream().sorted(Comparator.comparing(Juego::getId)).toList(); }
    public Optional<Juego> findById(Long id) { return Optional.ofNullable(data.get(id)); }
    public Optional<Juego> findByNombre(String nombre) {
        return data.values().stream().filter(j -> j.getNombre().equalsIgnoreCase(nombre)).findFirst();
    }
}
