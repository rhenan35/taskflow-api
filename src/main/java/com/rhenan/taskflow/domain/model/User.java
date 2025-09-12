package com.rhenan.taskflow.domain.model;

import com.rhenan.taskflow.domain.valueObjects.Email;
import com.rhenan.taskflow.domain.valueObjects.UserId;

import java.util.Objects;

public class User {

    private final UserId id;
    private final String name;
    private final Email email;

    private User(UserId id, String name, Email email) {
        this.id = Objects.requireNonNull(id, "id não pode ser nulo!");
        this.name = validateName(name);
        this.email = Objects.requireNonNull(email, "email não pode ser nulo!");
    }
    
    private String validateName(String name) {
        Objects.requireNonNull(name, "Name não pode ser nulo");
        String trimmedName = name.trim();
        if (trimmedName.isBlank()) {
            throw new IllegalArgumentException("Name não pode ser vazio");
        }
        if (trimmedName.length() > 150) {
            throw new IllegalArgumentException("Name passou de 150 caracteres");
        }
        return trimmedName;
    }

    public static User newUser(String name, Email email) {
        return new User(UserId.newUser(), name, email);
    }

    public UserId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User user = (User) obj;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
