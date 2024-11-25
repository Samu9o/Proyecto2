package src.com.learningpath.activities;

import src.com.learningpath.users.Student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SurveyResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Student student;
    private List<String> answers;

    public SurveyResponse(Student student) {
        this.student = student;
        this.answers = new ArrayList<>();
    }

    // Getters y Setters

    public Student getStudent() {
        return student;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void addAnswer(String answer) {
        answers.add(answer);
    }
}
