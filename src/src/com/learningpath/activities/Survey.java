package src.com.learningpath.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Survey extends Activity implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<SurveyQuestion> surveyQuestions;
    private List<SurveyResponse> surveyResponses;

    public Survey(String title, String description, String objective, int difficultyLevel, int expectedDuration, boolean isMandatory) {
        super(title, description, objective, difficultyLevel, expectedDuration, isMandatory);
        this.surveyQuestions = new ArrayList<>();
        this.surveyResponses = new ArrayList<>();
    }

    // Métodos para gestionar preguntas y respuestas

    public void addSurveyQuestion(SurveyQuestion question) {
        surveyQuestions.add(question);
    }

    public List<SurveyQuestion> getSurveyQuestions() {
        return surveyQuestions;
    }

    public void addSurveyResponse(SurveyResponse response) {
        surveyResponses.add(response);
    }

    public List<SurveyResponse> getSurveyResponses() {
        return surveyResponses;
    }

    @Override
    public String getType() {
        return "Survey";
    }
}
