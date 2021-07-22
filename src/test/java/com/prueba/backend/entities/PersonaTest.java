package com.prueba.backend.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PersonaTest {

  private static ValidatorFactory validatorFactory;
  private static Validator validator;

  @BeforeEach
  public void createValidator() {
    validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
  }

  @Test
  void testNombreVacio() {
    var persona = new Persona();
    Set<ConstraintViolation<Persona>> violations = validator.validate(persona);
    assertThat(violations).isNotEmpty();
    assertThat(violations.iterator().next().getMessage())
        .containsIgnoringCase("de la persona no puede ser");

    persona.setNombre("12");
    violations = validator.validate(persona);
    assertThat(violations).isNotEmpty();
    assertThat(violations.iterator().next().getMessage())
        .containsIgnoringCase("El nombre de la persona debe tener");
  }
}