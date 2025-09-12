package com.rhenan.taskflow.domain.model;

import com.rhenan.taskflow.domain.valueObjects.Email;
import com.rhenan.taskflow.domain.valueObjects.Name;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    
    @Test
    void deveCriarNovoUsuario() {
        Name name = new Name("João Silva");
        Email email = Email.de("joao@email.com");
        
        User user = User.newUser(name, email);
        
        assertNotNull(user.getId());
        assertEquals(name, user.getName());
        assertEquals(email, user.getEmail());
    }
    
    @Test
    void naoDevePermitirNameNulo() {
        Email email = Email.de("joao@email.com");
        assertThrows(NullPointerException.class, () -> User.newUser(null, email));
    }
    
    @Test
    void naoDevePermitirEmailNulo() {
        Name name = new Name("João Silva");
        assertThrows(NullPointerException.class, () -> User.newUser(name, null));
    }
    
    @Test
    void deveSerIgualQuandoMesmoId() {
        Name name = new Name("João Silva");
        Email email = Email.de("joao@email.com");
        User user1 = User.newUser(name, email);
        User user2 = User.newUser(name, email);
        
        assertNotEquals(user1, user2);
        assertEquals(user1, user1);
    }
    
    @Test
    void deveGerarHashCodeConsistente() {
        Name name = new Name("João Silva");
        Email email = Email.de("joao@email.com");
        User user = User.newUser(name, email);
        
        int hash1 = user.hashCode();
        int hash2 = user.hashCode();
        
        assertEquals(hash1, hash2);
    }
}