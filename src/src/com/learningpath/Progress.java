package src.com.learningpath;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import src.com.learningpath.activities.Activity;
import src.com.learningpath.activities.ActivityStatus;
import src.com.learningpath.users.Student;

public class Progress implements Serializable {
    private Student student;
    private LearningPath learningPath;
    private Date startDate;
    private Date completionDate;
    private Map<Activity, ActivityStatus> activityStatuses;

    public Progress(Student student, LearningPath learningPath) {
        this.student = student;
        this.learningPath = learningPath;
        this.startDate = new Date();
        this.activityStatuses = new HashMap<>();
        for (Activity activity : learningPath.getActivities()) {
            activityStatuses.put(activity, ActivityStatus.PENDING);
        }
    }
    public Map<Activity, ActivityStatus> getActivityStatuses() {
        return this.activityStatuses;
    }

    public void updateActivityStatus(Activity activity, ActivityStatus status) {
        activityStatuses.put(activity, status);
    }

    public double calculateCompletionPercentage() {
        long totalMandatory = learningPath.getActivities().stream().filter(Activity::isMandatory).count();
        long completedMandatory = activityStatuses.entrySet().stream()
                .filter(e -> e.getValue() == ActivityStatus.COMPLETED && e.getKey().isMandatory())
                .count();
        return (double) completedMandatory / totalMandatory * 100;
    }
}

