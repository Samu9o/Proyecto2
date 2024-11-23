package src.com.learningpath.activities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public abstract class Activity implements Serializable {
    protected String title;
    protected String description;
    protected String objective;
    protected int difficultyLevel;
    protected int expectedDuration; // en minutos
    protected List<Activity> suggestedPrerequisites;
    protected Date deadline;
    protected boolean isMandatory;

    public Activity(String title, String description, String objective, int difficultyLevel, int expectedDuration, boolean isMandatory) {
        this.title = title;
        this.description = description;
        this.objective = objective;
        this.difficultyLevel = difficultyLevel;
        this.expectedDuration = expectedDuration;
        this.isMandatory = isMandatory;
    }
    public int getExpectedDuration() {
        return expectedDuration;
    }


    public String getTitle() {
        return title;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public abstract String getType();
}
