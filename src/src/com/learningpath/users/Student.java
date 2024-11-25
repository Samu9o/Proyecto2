package src.com.learningpath.users;

import java.io.Serializable;
import java.util.Objects;

/**
 * Clase que representa a un estudiante.
 */
public class Student extends User implements Serializable {
    private static final long serialVersionUID = 1L;

    // Otros campos específicos de Student si es necesario

    /**
     * Constructor para crear un estudiante.
     *
     * @param username Nombre de usuario.
     * @param password Contraseña.
     * @param name     Nombre completo.
     */
    public Student(String username, String password, String name) {
        super(username, password, name, Role.STUDENT);
    }

    // Implementaciones específicas de Student si es necesario

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        return username.equals(student.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
