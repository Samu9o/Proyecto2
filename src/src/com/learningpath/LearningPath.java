package src.com.learningpath;

import src.com.learningpath.activities.Activity;
import src.com.learningpath.users.Teacher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LearningPath implements Serializable {
    private String title;
    private String description;
    private String objectives;
    private int difficultyLevel;
    private int duration; // en minutos
    private double rating;
    private Date creationDate;
    private Date modificationDate;
    private String version;
    private Teacher creator;
    public List<Activity> activities;

    public LearningPath(String title, String description, String objectives, int difficultyLevel, Teacher creator) {
        this.title = title;
        this.description = description;
        this.objectives = objectives;
        this.difficultyLevel = difficultyLevel;
        this.creator = creator;
        this.activities = new ArrayList<>();
        this.creationDate = new Date();
        this.modificationDate = new Date();
        this.version = "1.0";
    }

    public String getTitle() {
        return this.title;
    }

    public Teacher getCreator() {
        return this.creator;
    }
    
    public int getDuration() {
        return this.duration;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public String getVersion() {
        return this.version;
    }


    public List<Activity> getActivities() {
        return activities;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
        recalculateDuration();
    }

    private void recalculateDuration() {
    	this.duration = activities.stream().mapToInt(Activity::getExpectedDuration).sum();

    }
}

