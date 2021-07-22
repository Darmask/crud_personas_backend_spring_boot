package com.prueba.backend.entities;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "personas")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Persona extends EntidadBase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "per_id")
  private int id;

  @Column(name = "per_nombre_completo")
  @NotNull(message = "El nombre no puede ser vacío")
  @Size(min = 1, max = 255, message = "El nombre debe tener mínimo un carácter y máximo 255")
  private String nombre;

  @Column(name = "per_fecha_nacimiento")
  @NotNull(message = "La fecha de nacimiento no puede ser vacío")
  private LocalDate fechaNacimiento;

}
