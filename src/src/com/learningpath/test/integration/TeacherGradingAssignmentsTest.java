package src.com.learningpath.test.integration;

import src.com.learningpath.LearningPath;
import src.com.learningpath.Progress;
import src.com.learningpath.activities.ActivityStatus;
import src.com.learningpath.activities.Assignment;
import src.com.learningpath.users.Student;
import src.com.learningpath.users.Teacher;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TeacherGradingAssignmentsTest {

    @Test
    public void testTeacherGradingAssignments() {
        // Crear un profesor y un learning path
        Teacher teacher = new Teacher("teacher2", "pass", "Teacher Two");
        LearningPath lp = new LearningPath("Data Science", "Learn Data Science", "Understand data analysis", 2, teacher);

        // Agregar una tarea al learning path
        Assignment assignment = new Assignment("Data Analysis Project", "Analyze a dataset", "Apply skills", 2, 120, true, "Submit report");
        lp.addActivity(assignment);

        // Crear un estudiante y que se inscriba en el learning path
        Student student = new Student("student2", "pass", "Student Two");
        Progress progress = new Progress(student, lp);

        // El estudiante entrega la tarea (actualizamos el estado a SUBMITTED)
        progress.updateActivityStatus(assignment, ActivityStatus.SUBMITTED);

        // El profesor revisa y califica la tarea (actualizamos el estado a COMPLETED)
        progress.updateActivityStatus(assignment, ActivityStatus.COMPLETED);

        // Verificar que el estado de la actividad es COMPLETED
        assertEquals(ActivityStatus.COMPLETED, progress.getActivityStatuses().get(assignment));

        // Opcional: agregar calificación y retroalimentación (necesitaría modificar la clase Progress)
    }
}
