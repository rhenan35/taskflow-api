package com.rhenan.taskflow.domain.model;

import com.rhenan.taskflow.domain.valueObjects.Email;
import com.rhenan.taskflow.domain.valueObjects.Name;
import com.rhenan.taskflow.domain.valueObjects.UserId;

import java.util.Objects;

public class User {

    private final UserId id;
    private final Name name;
    private final Email email;

    private User(UserId id, Name name, Email email) {
        this.id = Objects.requireNonNull(id, "id não pode ser nulo!");
        this.name = Objects.requireNonNull(name, "name não pode ser nulo!");
        this.email = Objects.requireNonNull(email, "email não pode ser nulo!");
    }

    public static User newUser(Name name, Email email) {
        return new User(UserId.newUser(), name, email);
    }

    public UserId getId() {
        return id;
    }

    public Name getName() {
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
