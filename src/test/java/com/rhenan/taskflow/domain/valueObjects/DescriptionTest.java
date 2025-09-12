package com.rhenan.taskflow.domain.valueObjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DescriptionTest {
    
    @Test
    void deveCriarDescricaoValida() {
        String value = "Descrição válida";
        Description description = new Description(value);
        
        assertEquals(value, description.value());
    }
    
    @Test
    void devePermitirDescricaoNula() {
        Description description = new Description(null);
        
        assertNull(description.value());
    }
    
    @Test
    void deveRemoverEspacosExtras() {
        Description description = new Description("  Descrição com espaços  ");
        
        assertEquals("Descrição com espaços", description.value());
    }
    
    @Test
    void devePermitirDescricaoVazia() {
        Description description = new Description("");
        
        assertEquals("", description.value());
    }
    
    @Test
    void deveCriarDescricaoVaziaComMetodoStatico() {
        Description description = Description.empty();
        
        assertEquals("", description.value());
    }
    
    @Test
    void deveSerIgualQuandoMesmoValor() {
        Description desc1 = new Description("Mesma descrição");
        Description desc2 = new Description("Mesma descrição");
        
        assertEquals(desc1, desc2);
        assertEquals(desc1.hashCode(), desc2.hashCode());
    }
    
    @Test
    void deveSerIgualQuandoAmbasNulas() {
        Description desc1 = new Description(null);
        Description desc2 = new Description(null);
        
        assertEquals(desc1, desc2);
        assertEquals(desc1.hashCode(), desc2.hashCode());
    }
}