package com.rhenan.taskflow.domain.model;

import com.rhenan.taskflow.domain.valueObjects.Email;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    
    @Test
    void deveCriarNovoUsuario() {
        Email email = Email.de("joao@email.com");
        
        User user = User.newUser("João Silva", email);
        
        assertNotNull(user.getId());
        assertEquals("João Silva", user.getName());
        assertEquals(email, user.getEmail());
    }
    
    @Test
    void naoDevePermitirNameNulo() {
        Email email = Email.de("joao@email.com");
        assertThrows(NullPointerException.class, () -> User.newUser(null, email));
    }
    
    @Test
    void naoDevePermitirEmailNulo() {
        assertThrows(NullPointerException.class, () -> User.newUser("João Silva", null));
    }
    
    @Test
    void deveSerIgualQuandoMesmoId() {
        Email email = Email.de("joao@email.com");
        User user1 = User.newUser("João Silva", email);
        User user2 = User.newUser("João Silva", email);
        
        assertNotEquals(user1, user2);
        assertEquals(user1, user1);
    }
    
    @Test
    void deveGerarHashCodeConsistente() {
        Email email = Email.de("joao@email.com");
        User user = User.newUser("João Silva", email);
        
        int hash1 = user.hashCode();
        int hash2 = user.hashCode();
        
        assertEquals(hash1, hash2);
    }
}