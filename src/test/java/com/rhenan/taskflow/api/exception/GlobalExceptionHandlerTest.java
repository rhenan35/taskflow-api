package com.rhenan.taskflow.api.exception;

import com.rhenan.taskflow.domain.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void deveRetornarNotFoundParaNotFoundException() {
        String mensagem = "Recurso não encontrado";
        NotFoundException exception = new NotFoundException(mensagem);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getStatus());
        assertEquals("Not Found", response.getBody().getError());
        assertEquals(mensagem, response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
        assertTrue(response.getBody().getTimestamp().isBefore(Instant.now().plusSeconds(1)));
    }

    @Test
    void deveRetornarBadRequestParaValidationException() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "test");
        bindingResult.addError(new FieldError("test", "name", "Nome é obrigatório"));
        bindingResult.addError(new FieldError("test", "email", "Email inválido"));
        
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Validation Failed", response.getBody().getError());
        assertEquals("Dados inválidos", response.getBody().getMessage());
        assertNotNull(response.getBody().getValidationErrors());
        assertEquals(2, response.getBody().getValidationErrors().size());
        assertEquals("Nome é obrigatório", response.getBody().getValidationErrors().get("name"));
        assertEquals("Email inválido", response.getBody().getValidationErrors().get("email"));
    }

    @Test
    void deveRetornarBadRequestParaIllegalArgumentException() {
        String mensagem = "Argumento inválido";
        IllegalArgumentException exception = new IllegalArgumentException(mensagem);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Bad Request", response.getBody().getError());
        assertEquals(mensagem, response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void deveRetornarBadRequestParaHttpMessageNotReadableException() {
        HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);
        when(exception.getMessage()).thenReturn("JSON parse error");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleHttpMessageNotReadableException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getStatus());
        assertEquals("Bad Request", response.getBody().getError());
        assertEquals("Formato JSON inválido. Verifique se não há caracteres especiais não escapados no conteúdo.", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void deveRetornarInternalServerErrorParaExcecaoGenerica() {
        RuntimeException exception = new RuntimeException("Erro inesperado");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getStatus());
        assertEquals("Internal Server Error", response.getBody().getError());
        assertEquals("Erro interno do servidor", response.getBody().getMessage());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    void deveManterConsistenciaNoTimestamp() {
        NotFoundException exception = new NotFoundException("Teste");
        Instant antes = Instant.now();
        
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleNotFoundException(exception);
        
        Instant depois = Instant.now();
        assertNotNull(response.getBody().getTimestamp());
        assertTrue(response.getBody().getTimestamp().isAfter(antes.minusSeconds(1)));
        assertTrue(response.getBody().getTimestamp().isBefore(depois.plusSeconds(1)));
    }

    @Test
    void deveRetornarValidationErrorsVazioQuandoNaoHouverErros() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "test");
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationException(exception);

        assertNotNull(response.getBody().getValidationErrors());
        assertTrue(response.getBody().getValidationErrors().isEmpty());
    }

    @Test
    void deveManterMensagemOriginalDaNotFoundException() {
        String mensagemEspecifica = "Usuário com ID 123 não foi encontrado";
        NotFoundException exception = new NotFoundException(mensagemEspecifica);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleNotFoundException(exception);

        assertEquals(mensagemEspecifica, response.getBody().getMessage());
    }

    @Test
    void deveManterMensagemOriginalDaIllegalArgumentException() {
        String mensagemEspecifica = "Status inválido: UNKNOWN";
        IllegalArgumentException exception = new IllegalArgumentException(mensagemEspecifica);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalArgumentException(exception);

        assertEquals(mensagemEspecifica, response.getBody().getMessage());
    }
}