package src.com.learningpath.users;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase que representa a un profesor.
 */
public class Teacher extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    // Otros campos específicos de Teacher si es necesario

    /**
     * Constructor para crear un profesor.
     *
     * @param username Nombre de usuario.
     * @param password Contraseña.
     * @param name     Nombre completo.
     */
    public Teacher(String username, String password, String name) {
        super(username, password, name, Role.TEACHER);
    }

    // Implementaciones específicas de Teacher si es necesario

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Teacher teacher = (Teacher) o;

        return username.equals(teacher.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
