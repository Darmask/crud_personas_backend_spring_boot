package com.prueba.backend.repository;

import com.prueba.backend.entities.Persona;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Integer> {

  Optional<Persona> findByNombre(String nombre);
}
