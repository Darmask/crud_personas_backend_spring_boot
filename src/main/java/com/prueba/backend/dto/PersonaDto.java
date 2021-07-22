package com.prueba.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.prueba.backend.entities.Persona;
import java.time.LocalDate;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PersonaDto {

  private int id;

  @NotNull(message = "El nombre no puede ser vacío")
  @Size(min = 1, max = 255, message = "El nombre debe tener mínimo un carácter y máximo 255")
  private String nombre;

  @Column(name = "per_fecha_nacimiento")
  @NotNull(message = "La fecha de nacimiento  no puede ser vacío")
  @JsonFormat(pattern = "yyyy/MM/dd")
  private LocalDate fechaNacimiento;

}
