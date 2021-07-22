package com.prueba.backend.service.impl;

import com.prueba.backend.entities.Persona;
import com.prueba.backend.exceptions.ObjetoExistenteException;
import com.prueba.backend.exceptions.ObjetoNoExisteException;
import com.prueba.backend.repository.PersonaRepository;
import com.prueba.backend.service.PersonaService;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class PersonaServiceImpl implements PersonaService {

  private PersonaRepository personaRepository;
  private Validator validator;

  @Autowired
  public PersonaServiceImpl(PersonaRepository personaRepository, Validator validator) {
    this.personaRepository = personaRepository;
    this.validator = validator;
  }

  @Override
  public Persona crear(Persona persona) {
    this.validar(persona);
    this.personaRepository.save(persona);
    return persona;
  }

  @Override
  public Persona actualizar(Persona persona) {
    this.buscarPorId(persona.getId());
    this.validar(persona);
    this.personaRepository.save(persona);
    return persona;
  }

  @Override
  public Persona buscarPorId(int id) {
    return this.personaRepository
        .findById(id)
        .orElseThrow(() -> new ObjetoNoExisteException("No existe la persona con id " + id));
  }

  @Override
  public List<Persona> listar() {
    return this.personaRepository.findAll(Sort.by("id"));
  }

  @Override
  public void borrar(int id) {
    this.buscarPorId(id);
    this.personaRepository.deleteById(id);
  }

  private void validar(Persona persona) {
    Set<ConstraintViolation<Persona>> violations = validator.validate(persona);
    if (!violations.isEmpty()) {
      var sb = new StringBuilder();
      for (ConstraintViolation<Persona> constraintViolation : violations) {
        sb.append(constraintViolation.getMessage());
      }
      throw new ConstraintViolationException("Error occurred: " + sb, violations);
    }
    this.validarExistePorNombre(persona);
  }

  private void validarExistePorNombre(Persona persona) {
    Optional<Persona> personaExistente = this.personaRepository.findByNombre(persona.getNombre());
    if (personaExistente.isPresent() && !personaExistente.get().esElMismoId(persona)) {
      throw new ObjetoExistenteException("Ya existe la persona " + persona.getNombre());
    }
  }
}
