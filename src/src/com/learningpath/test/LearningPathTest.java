package src.com.learningpath.test;

import src.com.learningpath.LearningPath;
import src.com.learningpath.activities.Assignment;
import src.com.learningpath.activities.ResourceReview;
import src.com.learningpath.activities.Activity;
import src.com.learningpath.users.Teacher;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LearningPathTest {

    @Test
    public void testAddActivityAndRecalculateDuration() {
        Teacher teacher = new Teacher("jdoe", "password123", "John Doe");
        LearningPath lp = new LearningPath("Java Basics", "Introduction to Java", "Learn the basics of Java", 1, teacher);

        Activity activity1 = new ResourceReview("Variables in Java", "Learn about variables", "Understand variables", 1, 30, true, "http://example.com/variables");
        Activity activity2 = new Assignment("Hello World Program", "Write a Hello World program", "Get familiar with syntax", 1, 60, true, "Submit the .java file");

        lp.addActivity(activity1);
        lp.addActivity(activity2);

        // Verificamos que las actividades se hayan agregado correctamente
        assertEquals(2, lp.getActivities().size());

        // Verificamos que la duración total se haya recalculado correctamente
        assertEquals(90, lp.getDuration());
    }

    @Test
    public void testConstructorInitializesFieldsCorrectly() {
        Teacher teacher = new Teacher("jdoe", "password123", "John Doe");
        LearningPath lp = new LearningPath("Advanced Java", "Deep dive into Java", "Master advanced topics", 3, teacher);

        assertEquals("Advanced Java", lp.getTitle());
        assertEquals(teacher, lp.getCreator());
        assertNotNull(lp.getCreationDate());
        assertEquals("1.0", lp.getVersion());
    }
}
