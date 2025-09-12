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
    @SuppressWarnings("null")
    void deveRetornarNotFoundParaNotFoundException() {
        String mensagem = "Recurso não encontrado";
        NotFoundException exception = new NotFoundException(mensagem);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponse body = response.getBody();
        assertEquals(404, body.getStatus());
        assertEquals("Not Found", body.getError());
        assertEquals(mensagem, body.getMessage());
        assertNotNull(body.getTimestamp());
        assertTrue(body.getTimestamp().isBefore(Instant.now().plusSeconds(1)));
    }

    @Test
    @SuppressWarnings("null")
    void deveRetornarBadRequestParaValidationException() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "test");
        bindingResult.addError(new FieldError("test", "name", "Nome é obrigatório"));
        bindingResult.addError(new FieldError("test", "email", "Email inválido"));
        
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponse body = response.getBody();
        assertEquals(400, body.getStatus());
        assertEquals("Validation Failed", body.getError());
        assertEquals("Dados inválidos", body.getMessage());
        assertNotNull(body.getValidationErrors());
        assertEquals(2, body.getValidationErrors().size());
        assertEquals("Nome é obrigatório", body.getValidationErrors().get("name"));
        assertEquals("Email inválido", body.getValidationErrors().get("email"));
    }

    @Test
    @SuppressWarnings("null")
    void deveRetornarBadRequestParaIllegalArgumentException() {
        String mensagem = "Argumento inválido";
        IllegalArgumentException exception = new IllegalArgumentException(mensagem);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalArgumentException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponse body = response.getBody();
        assertEquals(400, body.getStatus());
        assertEquals("Bad Request", body.getError());
        assertEquals(mensagem, body.getMessage());
        assertNotNull(body.getTimestamp());
    }

    @Test
    @SuppressWarnings("null")
    void deveRetornarBadRequestParaHttpMessageNotReadableException() {
        HttpMessageNotReadableException exception = mock(HttpMessageNotReadableException.class);
        when(exception.getMessage()).thenReturn("JSON parse error");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleHttpMessageNotReadableException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponse body = response.getBody();
        assertEquals(400, body.getStatus());
        assertEquals("Bad Request", body.getError());
        assertEquals("Formato JSON inválido. Verifique se não há caracteres especiais não escapados no conteúdo.", body.getMessage());
        assertNotNull(body.getTimestamp());
    }

    @Test
    @SuppressWarnings("null")
    void deveRetornarInternalServerErrorParaExcecaoGenerica() {
        RuntimeException exception = new RuntimeException("Erro inesperado");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        ErrorResponse body = response.getBody();
        assertEquals(500, body.getStatus());
        assertEquals("Internal Server Error", body.getError());
        assertEquals("Erro interno do servidor", body.getMessage());
        assertNotNull(body.getTimestamp());
    }

    @Test
    @SuppressWarnings("null")
    void deveManterConsistenciaNoTimestamp() {
        NotFoundException exception = new NotFoundException("Teste");
        Instant antes = Instant.now();
        
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleNotFoundException(exception);
        
        Instant depois = Instant.now();
        assertNotNull(response.getBody());
        ErrorResponse body = response.getBody();
        assertNotNull(body.getTimestamp());
        assertTrue(body.getTimestamp().isAfter(antes.minusSeconds(1)));
        assertTrue(body.getTimestamp().isBefore(depois.plusSeconds(1)));
    }

    @Test
    @SuppressWarnings("null")
    void deveRetornarValidationErrorsVazioQuandoNaoHouverErros() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "test");
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationException(exception);

        assertNotNull(response.getBody());
        ErrorResponse body = response.getBody();
        assertNotNull(body.getValidationErrors());
        assertTrue(body.getValidationErrors().isEmpty());
    }

    @Test
    @SuppressWarnings("null")
    void deveManterMensagemOriginalDaNotFoundException() {
        String mensagemEspecifica = "Usuário com ID 123 não foi encontrado";
        NotFoundException exception = new NotFoundException(mensagemEspecifica);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleNotFoundException(exception);

        assertNotNull(response.getBody());
        ErrorResponse body = response.getBody();
        assertEquals(mensagemEspecifica, body.getMessage());
    }

    @Test
    @SuppressWarnings("null")
    void deveManterMensagemOriginalDaIllegalArgumentException() {
        String mensagemEspecifica = "Status inválido: UNKNOWN";
        IllegalArgumentException exception = new IllegalArgumentException(mensagemEspecifica);

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleIllegalArgumentException(exception);

        assertNotNull(response.getBody());
        ErrorResponse body = response.getBody();
        assertEquals(mensagemEspecifica, body.getMessage());
    }
}