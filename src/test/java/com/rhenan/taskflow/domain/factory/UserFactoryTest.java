package com.rhenan.taskflow.domain.factory;

import com.rhenan.taskflow.domain.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserFactoryTest {
    
    @Test
    void deveCriarUsuarioComNomeEEmail() {
        String name = "João Silva";
        String email = "joao@email.com";
        
        User user = UserFactory.createUser(name, email);
        
        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals(name, user.getName().value());
        assertEquals(email, user.getEmail().value());
    }
    
    @Test
    void naoDevePermitirNomeNulo() {
        assertThrows(NullPointerException.class, () -> 
            UserFactory.createUser(null, "joao@email.com")
        );
    }
    
    @Test
    void naoDevePermitirNomeVazio() {
        assertThrows(IllegalArgumentException.class, () -> 
            UserFactory.createUser("", "joao@email.com")
        );
    }
    
    @Test
    void naoDevePermitirNomeApenasEspacos() {
        assertThrows(IllegalArgumentException.class, () -> 
            UserFactory.createUser("   ", "joao@email.com")
        );
    }
    
    @Test
    void naoDevePermitirEmailNulo() {
        assertThrows(NullPointerException.class, () -> 
            UserFactory.createUser("João Silva", null)
        );
    }
    
    @Test
    void naoDevePermitirEmailVazio() {
        assertThrows(IllegalArgumentException.class, () -> 
            UserFactory.createUser("João Silva", "")
        );
    }
    
    @Test
    void naoDevePermitirEmailInvalido() {
        assertThrows(IllegalArgumentException.class, () -> 
            UserFactory.createUser("João Silva", "email-invalido")
        );
    }
    
    @Test
    void deveAceitarEmailsValidos() {
        assertDoesNotThrow(() -> UserFactory.createUser("João", "joao@email.com"));
        assertDoesNotThrow(() -> UserFactory.createUser("Maria", "maria.silva@empresa.com.br"));
        assertDoesNotThrow(() -> UserFactory.createUser("Pedro", "pedro123@test.org"));
    }
}