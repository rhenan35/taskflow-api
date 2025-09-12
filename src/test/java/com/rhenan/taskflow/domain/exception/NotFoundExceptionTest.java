package com.rhenan.taskflow.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NotFoundExceptionTest {
    
    @Test
    void deveCriarExcecaoComMensagem() {
        String message = "Recurso não encontrado";
        
        NotFoundException exception = new NotFoundException(message);
        
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }
    
    @Test
    void deveSerRuntimeException() {
        NotFoundException exception = new NotFoundException("Teste");
        
        assertInstanceOf(RuntimeException.class, exception);
    }
    
    @Test
    void devePermitirMensagemNula() {
        NotFoundException exception = new NotFoundException(null);
        
        assertNull(exception.getMessage());
    }
    
    @Test
    void devePermitirMensagemVazia() {
        NotFoundException exception = new NotFoundException("");
        
        assertEquals("", exception.getMessage());
    }
}