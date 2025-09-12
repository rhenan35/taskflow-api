package com.rhenan.taskflow.domain.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BusinessRuleExceptionTest {
    
    @Test
    void deveCriarExcecaoComMensagem() {
        String message = "Regra de negócio violada";
        
        BusinessRuleException exception = new BusinessRuleException(message);
        
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }
    
    @Test
    void deveSerRuntimeException() {
        BusinessRuleException exception = new BusinessRuleException("Teste");
        
        assertInstanceOf(RuntimeException.class, exception);
    }
    
    @Test
    void devePermitirMensagemNula() {
        BusinessRuleException exception = new BusinessRuleException(null);
        
        assertNull(exception.getMessage());
    }
    
    @Test
    void devePermitirMensagemVazia() {
        BusinessRuleException exception = new BusinessRuleException("");
        
        assertEquals("", exception.getMessage());
    }
}