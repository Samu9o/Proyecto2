package src.learningpath.users;

import java.io.Serializable;

public abstract class User implements Serializable {
    protected String username;
    protected String password;
    protected String name;

    public User(String username, String password, String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }

    // Añadir getName()
    public String getName() {
        return this.name;
    }

    public String getUsername() {
        return username;
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    public abstract String getRole();
}
