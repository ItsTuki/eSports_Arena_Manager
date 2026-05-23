package com.example.registrationservice.repository;

import com.example.registrationservice.model.Inscripcion;
import org.springframework.stereotype.Repository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public interface InscripcionRepository {
    Inscripcion save(Inscripcion inscripcion);
    List<Inscripcion> findAll();
    Optional<Inscripcion> findById(Long id);
}

@Repository
class InMemoryInscripcionRepository implements InscripcionRepository {
    private final ConcurrentHashMap<Long, Inscripcion> data = new ConcurrentHashMap<>();
    private final AtomicLong ids = new AtomicLong(1);
    public Inscripcion save(Inscripcion i) { if (i.getId() == null) i.setId(ids.getAndIncrement()); data.put(i.getId(), i); return i; }
    public List<Inscripcion> findAll() { return data.values().stream().sorted(Comparator.comparing(Inscripcion::getId)).toList(); }
    public Optional<Inscripcion> findById(Long id) { return Optional.ofNullable(data.get(id)); }
}
