package src.com.learningpath.test;

import src.com.learningpath.activities.Question;
import src.com.learningpath.activities.Quiz;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class QuizTest {

    @Test
    public void testQuizCreation() {
        Question q1 = new Question("What is Java?", new String[]{"Programming Language", "Coffee", "Animal", "Game"}, 0, "Java is a programming language.");
        Question q2 = new Question("What is JDK?", new String[]{"Java Development Kit", "Java Deployment Kit", "Just Do it Kit", "None"}, 0, "JDK stands for Java Development Kit.");

        Quiz quiz = new Quiz("Java Basics Quiz", "Test your Java basics", "Assess basic knowledge", 1, 20, true, Arrays.asList(q1, q2), 70.0);

        assertEquals(2, quiz.getQuestions().size());
        assertEquals(70.0, quiz.getPassingScore());
        assertEquals("Quiz", quiz.getType());
    }

    @Test
    public void testQuestionCorrectOptionIndex() {
        Question q = new Question("Select the correct answer", new String[]{"Option A", "Option B", "Option C", "Option D"}, 2, "Option C is correct.");

        assertEquals(2, q.getCorrectOptionIndex());
        assertEquals("Option C", q.getOptions()[q.getCorrectOptionIndex()]);
    }
}
