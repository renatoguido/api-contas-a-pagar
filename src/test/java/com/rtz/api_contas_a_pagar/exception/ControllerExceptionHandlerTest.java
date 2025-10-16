package com.rtz.api_contas_a_pagar.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ControllerExceptionHandlerTest {


    private ControllerExceptionHandler handler;

    @BeforeEach
    void setup() {
        handler = new ControllerExceptionHandler();
    }

    @Test
    void testHandleContaNotFoundException() {
        ContaNotFoundException ex = new ContaNotFoundException("Conta não encontrada");
        ResponseEntity<Map<String, Object>> response = handler.handleContaNotFoundException(ex);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Conta não encontrada", response.getBody().get("message"));
        assertEquals("Not Found", response.getBody().get("error"));
        assertNotNull(response.getBody().get("timestamp"));
    }

    @Test
    void testHandleRuntimeException() {
        RuntimeException ex = new RuntimeException("Erro inesperado");
        ResponseEntity<Map<String, Object>> response = handler.handleRuntimeException(ex);

        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Erro inesperado", response.getBody().get("message"));
        assertEquals("Internal Server Error", response.getBody().get("error"));
    }

    @Test
    void testHandleValidationExceptions() {
        // Mock de BindingResult e FieldError
        FieldError fieldError = new FieldError("contaRequestDTO", "nome", "não pode ser nulo");
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);
        ResponseEntity<Map<String, Object>> response = handler.handleValidationExceptions(ex);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Erro de validação", response.getBody().get("message"));
        Map<String, String> details = (Map<String, String>) response.getBody().get("details");
        assertTrue(details.containsKey("nome"));
        assertEquals("não pode ser nulo", details.get("nome"));
    }

    @Test
    void testHandleConstraintViolation() {
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);

        jakarta.validation.Path pathMock = mock(jakarta.validation.Path.class);
        when(pathMock.toString()).thenReturn("valorOriginal");
        when(violation.getPropertyPath()).thenReturn(pathMock);

        when(violation.getMessage()).thenReturn("deve ser positivo");

        ConstraintViolationException ex = new ConstraintViolationException(Set.of(violation));
        ResponseEntity<Map<String, Object>> response = handler.handleConstraintViolation(ex);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Violação de restrições", response.getBody().get("message"));

        Map<String, String> details = (Map<String, String>) response.getBody().get("details");
        assertTrue(details.containsKey("valorOriginal"));
        assertEquals("deve ser positivo", details.get("valorOriginal"));
    }
}