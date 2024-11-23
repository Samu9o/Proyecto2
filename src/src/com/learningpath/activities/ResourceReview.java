package src.com.learningpath.activities;

public class ResourceReview extends Activity {
    private String resourceLink; // URL o referencia al recurso

    public ResourceReview(String title, String description, String objective, int difficultyLevel, int expectedDuration, boolean isMandatory, String resourceLink) {
        super(title, description, objective, difficultyLevel, expectedDuration, isMandatory);
        this.resourceLink = resourceLink;
    }

    public String getResourceLink() {
        return resourceLink;
    }

    @Override
    public String getType() {
        return "Resource Review";
    }
}
