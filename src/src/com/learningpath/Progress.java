package src.com.learningpath;

import src.com.learningpath.activities.Activity;
import src.com.learningpath.activities.ActivityStatus;
import src.com.learningpath.users.Student;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Progress implements Serializable {
    private static final long serialVersionUID = 1L;

    private Student student;
    private LearningPath learningPath;
    private Date startDate;
    private Date completionDate;
    private Map<Activity, ActivityStatus> activityStatuses;

    /**
     * Constructor de la clase Progress.
     *
     * @param student      El estudiante asociado al progreso.
     * @param learningPath El Learning Path asociado al progreso.
     */
    public Progress(Student student, LearningPath learningPath) {
        this.student = student;
        this.learningPath = learningPath;
        this.startDate = new Date();
        this.activityStatuses = new HashMap<>();
        for (Activity activity : learningPath.getActivities()) {
            activityStatuses.put(activity, ActivityStatus.PENDING);
        }
    }

    /**
     * Actualiza el estado de una actividad específica.
     *
     * @param activity La actividad a actualizar.
     * @param status   El nuevo estado de la actividad.
     */
    public void updateActivityStatus(Activity activity, ActivityStatus status) {
        activityStatuses.put(activity, status);
        if (status == ActivityStatus.COMPLETED || status == ActivityStatus.FAILED) {
            checkCompletion();
        }
    }

    /**
     * Calcula el porcentaje de actividades obligatorias completadas.
     *
     * @return El porcentaje de finalización.
     */
    public double calculateCompletionPercentage() {
        long totalMandatory = learningPath.getActivities().stream().filter(Activity::isMandatory).count();
        if (totalMandatory == 0) return 100.0;
        long completedMandatory = activityStatuses.entrySet().stream()
                .filter(e -> e.getValue() == ActivityStatus.COMPLETED && e.getKey().isMandatory())
                .count();
        return (double) completedMandatory / totalMandatory * 100;
    }

    /**
     * Verifica si todas las actividades obligatorias están completadas para establecer la fecha de finalización.
     */
    private void checkCompletion() {
        boolean allCompleted = learningPath.getActivities().stream()
                .filter(Activity::isMandatory)
                .allMatch(a -> activityStatuses.get(a) == ActivityStatus.COMPLETED);
        if (allCompleted && completionDate == null) {
            completionDate = new Date();
        }
    }

    // Getters para los campos privados

    /**
     * Obtiene el estudiante asociado al progreso.
     *
     * @return El estudiante.
     */
    public Student getStudent() {
        return this.student;
    }

    /**
     * Obtiene el Learning Path asociado al progreso.
     *
     * @return El Learning Path.
     */
    public LearningPath getLearningPath() {
        return this.learningPath;
    }

    /**
     * Obtiene la fecha de inicio del progreso.
     *
     * @return La fecha de inicio.
     */
    public Date getStartDate() {
        return this.startDate;
    }

    /**
     * Obtiene la fecha de finalización del progreso, si existe.
     *
     * @return La fecha de finalización, o null si aún no ha sido completado.
     */
    public Date getCompletionDate() {
        return this.completionDate;
    }

    /**
     * Obtiene el mapa de estados de las actividades.
     *
     * @return Un mapa que relaciona cada actividad con su estado actual.
     */
    public Map<Activity, ActivityStatus> getActivityStatuses() {
        return this.activityStatuses;
    }

    /**
     * Obtiene el estado de una actividad específica.
     *
     * @param activity La actividad de la cual se quiere obtener el estado.
     * @return El estado de la actividad.
     */
    public ActivityStatus getActivityStatus(Activity activity) {
        return activityStatuses.get(activity);
    }

    // Opcional: Puedes agregar métodos adicionales si lo necesitas, como setters o métodos de conveniencia.
}
