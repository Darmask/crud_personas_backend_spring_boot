package com.prueba.backend.controller;

import com.prueba.backend.dto.PersonaDto;
import com.prueba.backend.entities.Persona;
import com.prueba.backend.service.PersonaService;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/personas", produces = MediaType.APPLICATION_JSON_VALUE)
public class PersonaRestController {

  private PersonaService personaService;
  private ModelMapper modelMapper;

  @Autowired
  public PersonaRestController(PersonaService personaService, ModelMapper modelMapper) {
    this.personaService = personaService;
    this.modelMapper = modelMapper;
  }

  @GetMapping
  public ResponseEntity<List<PersonaDto>> listar() {
    List<PersonaDto> cargos = this.personaService.listar()
        .stream()
        .map(persona -> modelMapper.map(persona, PersonaDto.class))
        .collect(Collectors.toList());
    return new ResponseEntity<>(cargos, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<PersonaDto> crear(@Valid @RequestBody PersonaDto personaDto) {
    var persona = this.modelMapper.map(personaDto, Persona.class);
    persona = this.personaService.crear(persona);
    return new ResponseEntity<>(modelMapper.map(persona, PersonaDto.class), HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<PersonaDto> actualizar(@Valid @RequestBody PersonaDto personaDto,
      @PathVariable("id") int id) {
    if (personaDto == null || personaDto.getId() != id) {
      throw new RuntimeException("El id de la persona a actualizar no corresponde al path");
    }
    var persona = this.modelMapper.map(personaDto, Persona.class);
    persona = this.personaService.actualizar(persona);
    return new ResponseEntity<>(modelMapper.map(persona, PersonaDto.class), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity eliminar(@PathVariable("id") int id,
      @PathVariable("id") int idUsuario) {
    personaService.borrar(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
