package com.prueba.backend.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prueba.backend.entities.Persona;
import com.prueba.backend.exceptions.ObjetoExistenteException;
import com.prueba.backend.exceptions.ObjetoNoExisteException;
import com.prueba.backend.repository.PersonaRepository;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@ExtendWith(MockitoExtension.class)
class PersonaServiceImplTest {

  @Mock
  private static PersonaRepository personaRepository;

  @Mock
  private static LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();

  @InjectMocks
  private static PersonaServiceImpl personaService;

  private final static String NOMBRE_NO_EXISTENTE = "a";
  private final static String PRINCIPAL = "Carlos";
  private final static String SUPLENTE = "Tovar";
  private final static LocalDate FECHA_NACIMIENTO = LocalDate.now();

  @Test
  void cuando_crea_y_nombre_no_existe_deberia_crear_persona() {
    when(personaRepository.findByNombre(NOMBRE_NO_EXISTENTE)).thenReturn(Optional.ofNullable(null));
    var persona = this.construirPersona(0, NOMBRE_NO_EXISTENTE, FECHA_NACIMIENTO);

    personaService.crear(persona);

    verify(personaRepository, times(1))
        .findByNombre(persona.getNombre());
    verify(personaRepository, times(1))
        .save(persona);
  }

  @Test
  void cuando_crea_y_nombre_existe_deberia_lanzar_exception() {
    var persona = this.construirPersona(0, PRINCIPAL, FECHA_NACIMIENTO);
    this.configurarPersonaExistente(PRINCIPAL);

    assertThrows(ObjetoExistenteException.class, () -> personaService.crear(persona));

    verify(personaRepository, times(1))
        .findByNombre(persona.getNombre());
    verify(personaRepository, times(0))
        .save(persona);
  }

  @Test
  void cuando_actualiza_y_persona_no_existe_deberia_lanzar_exception() {
    var persona = this.construirPersona(10, PRINCIPAL, FECHA_NACIMIENTO);
    when(personaRepository.findById(persona.getId())).thenReturn(Optional.ofNullable(null));

    assertThrows(RuntimeException.class, () -> personaService.actualizar(persona));

    verify(personaRepository, times(1))
        .findById(persona.getId());
    verify(personaRepository, times(0))
        .save(persona);
  }

  @Test
  void cuando_actualiza_y_nombre_existe_deberia_lanzar_exception() {
    var persona = this.construirPersona(10, PRINCIPAL, FECHA_NACIMIENTO);
    this.configurarPersonaExistente(PRINCIPAL);
    when(personaRepository.findById(persona.getId())).thenReturn(Optional.of(persona));

    assertThrows(ObjetoExistenteException.class, () -> personaService.actualizar(persona));

    verify(personaRepository, times(1))
        .findById(persona.getId());
    verify(personaRepository, times(1))
        .findByNombre(PRINCIPAL);
    verify(personaRepository, times(0))
        .save(persona);
  }

  @Test
  void cuando_actualiza_con_mismo_nombre() {
    var persona = this.construirPersona(10, PRINCIPAL, FECHA_NACIMIENTO);
    when(personaRepository.findById(persona.getId())).thenReturn(Optional.of(persona));
    when(personaRepository.findByNombre(PRINCIPAL)).thenReturn(Optional.of(persona));

    personaService.actualizar(persona);

    verify(personaRepository, times(1))
        .findById(persona.getId());
    verify(personaRepository, times(1))
        .findByNombre(PRINCIPAL);
    verify(personaRepository, times(1))
        .save(persona);
  }

  @Test
  void cuando_existe_persona_buscar_por_id() {
    var persona = this.construirPersona(10, PRINCIPAL, FECHA_NACIMIENTO);

    when(personaRepository.findById(persona.getId()))
        .thenReturn(Optional.of(persona));

    var personaEncontrado = personaService.buscarPorId(persona.getId());
    assertThat(personaEncontrado).isNotNull();
    assertThat(personaEncontrado.getId()).isEqualTo(10);
  }

  @Test
  void cuando_no_existe_persona_buscar_por_id() {
    Optional<Persona> personaNoExistente = Optional.ofNullable(null);
    when(personaRepository.findById(5)).thenReturn(personaNoExistente);

    assertThrows(ObjetoNoExisteException.class, () -> personaService.buscarPorId(5));

    verify(personaRepository, times(1)).findById(5);
  }

  @Test
  void listar() {
    Sort sort = Sort.by("id");
    when(personaRepository.findAll(sort)).thenReturn(this.obtenerListaPersonas());

    List<Persona> personas = personaService.listar();

    assertThat(personas).isNotNull().isNotEmpty();
    verify(personaRepository, times(1)).findAll(sort);
  }

  @Test
  void cuando_borrar_persona_existe() {
    var persona = this.construirPersona(10, PRINCIPAL, FECHA_NACIMIENTO);
    Optional<Persona> personaExistente = Optional.of(persona);

    when(personaRepository.findById(10)).thenReturn(personaExistente);

    personaService.borrar(10);
    verify(personaRepository, times(1)).deleteById(10);
  }

  @Test
  void cuando_borrar_persona_no_existe() {
    var persona = this.construirPersona(10, PRINCIPAL, FECHA_NACIMIENTO);
    Optional<Persona> personaExistente = Optional.ofNullable(null);

    when(personaRepository.findById(10)).thenReturn(personaExistente);

    assertThrows(ObjetoNoExisteException.class, () -> personaService.borrar(10));
    verify(personaRepository, times(0)).deleteById(10);
    verify(personaRepository, times(1)).findById(10);
  }

  private Persona construirPersona(int id, String nombre, LocalDate fechaNacimiento) {
    var persona = new Persona();
    persona.setId(id);
    persona.setNombre(nombre);
    persona.setFechaNacimiento(fechaNacimiento);
    return persona;
  }

  private List<Persona> obtenerListaPersonas() {
    var persona1 = this.construirPersona(1, PRINCIPAL, FECHA_NACIMIENTO);
    var persona2 = this.construirPersona(2, SUPLENTE, FECHA_NACIMIENTO);
    return Arrays.asList(persona1, persona2);
  }

  private void configurarPersonaExistente(String nombre) {
    var personaExistenteConMismoNombre = new Persona();
    personaExistenteConMismoNombre.setId(100);
    personaExistenteConMismoNombre.setNombre(nombre);
    when(personaRepository.findByNombre(nombre))
        .thenReturn(Optional.of(personaExistenteConMismoNombre));
  }
}