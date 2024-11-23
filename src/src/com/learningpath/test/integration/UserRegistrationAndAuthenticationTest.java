package src.com.learningpath.test.integration;

import src.com.learningpath.users.Student;
import src.com.learningpath.users.Teacher;
import src.com.learningpath.users.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class UserRegistrationAndAuthenticationTest {

    @Test
    public void testUserRegistrationAndAuthentication() {
        List<User> users = new ArrayList<>();

        // Registro de profesor
        Teacher teacher = new Teacher("prof123", "securePass", "Prof. John");
        users.add(teacher);

        // Registro de estudiante
        Student student = new Student("stud456", "pass123", "Student Mary");
        users.add(student);

        // Intentamos autenticarnos con credenciales correctas
        User foundTeacher = users.stream()
                .filter(u -> u.getUsername().equals("prof123"))
                .findFirst()
                .orElse(null);

        assertNotNull(foundTeacher);
        assertTrue(foundTeacher.authenticate("securePass"));

        User foundStudent = users.stream()
                .filter(u -> u.getUsername().equals("stud456"))
                .findFirst()
                .orElse(null);

        assertNotNull(foundStudent);
        assertTrue(foundStudent.authenticate("pass123"));

        // Intentamos autenticarnos con credenciales incorrectas
        assertFalse(foundTeacher.authenticate("wrongPass"));
        assertFalse(foundStudent.authenticate("123pass"));
    }
}
