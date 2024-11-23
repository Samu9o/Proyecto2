package src.com.learningpath.test;

import src.com.learningpath.LearningPath;
import src.com.learningpath.Progress;
import src.com.learningpath.activities.*;
import src.com.learningpath.users.Student;
import src.com.learningpath.users.Teacher;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProgressTest {

    @Test
    public void testCalculateCompletionPercentage() {
        Teacher teacher = new Teacher("jdoe", "password123", "John Doe");
        Student student = new Student("sstudent", "password456", "Sarah Student");
        LearningPath lp = new LearningPath("Data Structures", "Learn about data structures", "Understand lists, stacks, queues", 2, teacher);

        Activity activity1 = new ResourceReview("Lists", "Learn about lists", "Understand lists", 2, 45, true, "http://example.com/lists");
        Activity activity2 = new Assignment("Implement a Stack", "Create a stack in Java", "Understand stacks", 3, 90, true, "Submit code via platform");
        Activity activity3 = new Quiz("Queues Quiz", "Test your knowledge on queues", "Assess understanding", 2, 30, false, null, 70);

        lp.addActivity(activity1);
        lp.addActivity(activity2);
        lp.addActivity(activity3);

        Progress progress = new Progress(student, lp);

        // Inicialmente, el porcentaje de finalización debe ser 0%
        assertEquals(0.0, progress.calculateCompletionPercentage());

        // Actualizamos el estado de una actividad obligatoria a COMPLETED
        progress.updateActivityStatus(activity1, ActivityStatus.COMPLETED);

        // Ahora, el porcentaje debe ser 50% (1 de 2 actividades obligatorias)
        assertEquals(50.0, progress.calculateCompletionPercentage());

        // Completamos la segunda actividad obligatoria
        progress.updateActivityStatus(activity2, ActivityStatus.COMPLETED);

        // El porcentaje debe ser 100%
        assertEquals(100.0, progress.calculateCompletionPercentage());
    }

    @Test
    public void testUpdateActivityStatus() {
        Teacher teacher = new Teacher("jdoe", "password123", "John Doe");
        Student student = new Student("sstudent", "password456", "Sarah Student");
        LearningPath lp = new LearningPath("Algorithms", "Learn about algorithms", "Understand sorting algorithms", 3, teacher);

        Activity activity = new ResourceReview("Sorting Algorithms", "Learn about sorting", "Understand sorting", 3, 60, true, "http://example.com/sorting");
        lp.addActivity(activity);

        Progress progress = new Progress(student, lp);

        // Estado inicial debe ser PENDING
        assertEquals(ActivityStatus.PENDING, progress.getActivityStatuses().get(activity));

        // Actualizamos el estado a COMPLETED
        progress.updateActivityStatus(activity, ActivityStatus.COMPLETED);

        // Verificamos que el estado se haya actualizado
        assertEquals(ActivityStatus.COMPLETED, progress.getActivityStatuses().get(activity));
    }
}
