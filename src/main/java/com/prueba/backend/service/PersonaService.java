package com.prueba.backend.service;

import com.prueba.backend.entities.Persona;
import java.util.List;

public interface PersonaService {

  Persona crear(Persona persona);

  Persona actualizar(Persona persona);

  Persona buscarPorId(int id);

  List<Persona> listar();

  void borrar(int id);
}
