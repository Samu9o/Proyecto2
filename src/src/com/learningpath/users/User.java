package src.com.learningpath.users;

import java.io.Serializable;

/**
 * Clase abstracta que representa a un usuario en el sistema.
 */
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String username;
    protected String password;
    protected String name;
    protected Role role;

    /**
     * Constructor para crear un usuario.
     *
     * @param username Nombre de usuario.
     * @param password Contraseña.
     * @param name     Nombre completo.
     * @param role     Rol del usuario.
     */
    public User(String username, String password, String name, Role role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    // Getters y Setters

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    /**
     * Método para autenticar al usuario.
     *
     * @param password Contraseña a verificar.
     * @return True si la contraseña es correcta, false en caso contrario.
     */
    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    // Implementaciones de equals y hashCode si es necesario
}
