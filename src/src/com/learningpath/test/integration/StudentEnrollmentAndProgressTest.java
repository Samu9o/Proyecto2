package src.com.learningpath.test.integration;

import src.com.learningpath.LearningPath;
import src.com.learningpath.Progress;
import src.com.learningpath.activities.*;
import src.com.learningpath.users.Student;
import src.com.learningpath.users.Teacher;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class StudentEnrollmentAndProgressTest {

    @Test
    public void testStudentEnrollmentAndProgress() {
        // Crear un profesor y un learning path
        Teacher teacher = new Teacher("teacher1", "pass", "Teacher One");
        LearningPath lp = new LearningPath("Python Basics", "Learn Python", "Understand the basics of Python", 1, teacher);

        // Agregar actividades al learning path
        Activity activity1 = new ResourceReview("Introduction to Python", "Intro", "Get started", 1, 30, true, "http://python.org");
        Activity activity2 = new Assignment("First Program", "Write a program", "Practice coding", 1, 60, true, "Submit .py file");
        lp.addActivity(activity1);
        lp.addActivity(activity2);

        // Crear un estudiante y que se inscriba en el learning path
        Student student = new Student("student1", "pass", "Student One");
        Progress progress = new Progress(student, lp);

        // Verificar que el progreso inicial es 0%
        assertEquals(0.0, progress.calculateCompletionPercentage());

        // El estudiante completa la primera actividad
        progress.updateActivityStatus(activity1, ActivityStatus.COMPLETED);

        // Verificar que el progreso es ahora 50%
        assertEquals(50.0, progress.calculateCompletionPercentage());

        // El estudiante completa la segunda actividad
        progress.updateActivityStatus(activity2, ActivityStatus.COMPLETED);

        // Verificar que el progreso es ahora 100%
        assertEquals(100.0, progress.calculateCompletionPercentage());
    }
}
