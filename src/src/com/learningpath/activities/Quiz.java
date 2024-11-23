package src.com.learningpath.activities;

import java.util.List;

public class Quiz extends Activity {
    private List<Question> questions;
    private double passingScore;

    public Quiz(String title, String description, String objective, int difficultyLevel, int expectedDuration, boolean isMandatory, List<Question> questions, double passingScore) {
        super(title, description, objective, difficultyLevel, expectedDuration, isMandatory);
        this.questions = questions;
        this.passingScore = passingScore;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public double getPassingScore() {
        return passingScore;
    }

    @Override
    public String getType() {
        return "Quiz";
    }
}
	