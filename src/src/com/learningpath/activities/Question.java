package src.com.learningpath.activities;

import java.io.Serializable;

public class Question implements Serializable {
    private String questionText;
    private String[] options;
    private int correctOptionIndex;
    private String explanation;

    public Question(String questionText, String[] options, int correctOptionIndex, String explanation) {
        this.questionText = questionText;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
        this.explanation = explanation;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }

    public String getExplanation() {
        return explanation;
    }
}
