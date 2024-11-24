package src.com.learningpath.activities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public abstract class Activity implements Serializable {
    private static final long serialVersionUID = 1L;

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

    // Getters para los campos privados/protegidos

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getObjective() {
        return objective;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public int getExpectedDuration() {
        return expectedDuration;
    }

    public List<Activity> getSuggestedPrerequisites() {
        return suggestedPrerequisites;
    }

    public Date getDeadline() {
        return deadline;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    // Método abstracto para obtener el tipo de actividad
    public abstract String getType();

    // Sobrescribir equals y hashCode basados en title y description (asumiendo que juntos son únicos)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Activity)) return false;
        Activity other = (Activity) obj;
        return title.equals(other.title) && description.equals(other.description);
    }

    @Override
    public int hashCode() {
        return title.hashCode() * 31 + description.hashCode();
    }
}
