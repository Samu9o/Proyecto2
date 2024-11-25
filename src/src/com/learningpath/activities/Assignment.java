package src.com.learningpath.activities;

public class Assignment extends Activity {
    private String submissionInstructions;

    public Assignment(String title, String description, String objective, int difficultyLevel, int expectedDuration, boolean isMandatory, String submissionInstructions) {
        super(title, description, objective, difficultyLevel, expectedDuration, isMandatory);
        this.submissionInstructions = submissionInstructions;
    }

    public String getSubmissionInstructions() {
        return submissionInstructions;
    }

    @Override
    public String getType() {
        return "Assignment";
    }
    
}
	