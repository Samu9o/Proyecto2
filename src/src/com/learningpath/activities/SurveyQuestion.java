package src.com.learningpath.activities;

import java.io.Serializable;

public class SurveyQuestion implements Serializable {
    private static final long serialVersionUID = 1L;

    private String questionText;
    private String answer; // Respuesta del estudiante

    public SurveyQuestion(String questionText) {
        this.questionText = questionText;
        this.answer = "";
    }

    // Getters y Setters

    public String getQuestionText() {
        return questionText;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
