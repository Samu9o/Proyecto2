package src.com.learningpath;

import src.com.learningpath.activities.*;
import src.com.learningpath.users.Student;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase que representa el progreso de un estudiante en un Learning Path.
 */
public class Progress implements Serializable {
    private static final long serialVersionUID = 1L;

    private Student student;
    private LearningPath learningPath;
    private Map<Activity, ActivityStatus> activityStatuses;
    private Map<Survey, SurveyResponse> surveyResponses;
    private Map<OpenEndedExam, OpenEndedResponse> examResponses;

    /**
     * Constructor para crear un progreso de Learning Path.
     *
     * @param student      El estudiante.
     * @param learningPath El Learning Path.
     */
    public Progress(Student student, LearningPath learningPath) {
        this.student = student;
        this.learningPath = learningPath;
        this.activityStatuses = new HashMap<>();
        this.surveyResponses = new HashMap<>();
        this.examResponses = new HashMap<>();
        // Inicializar estados de actividades
        for (Activity activity : learningPath.getActivities()) {
            activityStatuses.put(activity, ActivityStatus.PENDING);
        }
    }

    // Getters y Setters

    /**
     * Obtiene el estudiante asociado a este progreso.
     *
     * @return El estudiante.
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Establece el estudiante asociado a este progreso.
     *
     * @param student El nuevo estudiante.
     */
    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     * Obtiene el Learning Path asociado a este progreso.
     *
     * @return El Learning Path.
     */
    public LearningPath getLearningPath() {
        return learningPath;
    }

    /**
     * Establece el Learning Path asociado a este progreso.
     *
     * @param learningPath El nuevo Learning Path.
     */
    public void setLearningPath(LearningPath learningPath) {
        this.learningPath = learningPath;
    }

    /**
     * Obtiene el estado de todas las actividades.
     *
     * @return El mapa de actividades y sus estados.
     */
    public Map<Activity, ActivityStatus> getActivityStatuses() {
        return activityStatuses;
    }

    /**
     * Obtiene las respuestas a las encuestas.
     *
     * @return El mapa de encuestas y sus respuestas.
     */
    public Map<Survey, SurveyResponse> getSurveyResponses() {
        return surveyResponses;
    }

    /**
     * Obtiene las respuestas a los exámenes de preguntas abiertas.
     *
     * @return El mapa de exámenes y sus respuestas.
     */
    public Map<OpenEndedExam, OpenEndedResponse> getExamResponses() {
        return examResponses;
    }

    // Métodos para gestionar actividades

    /**
     * Actualiza el estado de una actividad.
     *
     * @param activity La actividad a actualizar.
     * @param status   El nuevo estado.
     */
    public void updateActivityStatus(Activity activity, ActivityStatus status) {
        activityStatuses.put(activity, status);
    }

    /**
     * Obtiene el estado de una actividad.
     *
     * @param activity La actividad.
     * @return El estado de la actividad.
     */
    public ActivityStatus getActivityStatus(Activity activity) {
        return activityStatuses.get(activity);
    }

    /**
     * Calcula el porcentaje de actividades completadas.
     *
     * @return El porcentaje de completado.
     */
    public double calculateCompletionPercentage() {
        int total = activityStatuses.size();
        long completed = activityStatuses.values().stream()
                .filter(status -> status == ActivityStatus.COMPLETED || status == ActivityStatus.SUBMITTED)
                .count();
        return (double) completed / total * 100;
    }

    // Métodos para gestionar respuestas a encuestas

    /**
     * Añade una respuesta a una encuesta específica.
     *
     * @param survey  La encuesta.
     * @param response La respuesta del estudiante.
     */
    public void addSurveyResponse(Survey survey, SurveyResponse response) {
        surveyResponses.put(survey, response);
    }

    /**
     * Obtiene la respuesta de una encuesta específica.
     *
     * @param survey La encuesta.
     * @return La respuesta del estudiante.
     */
    public SurveyResponse getSurveyResponse(Survey survey) {
        return surveyResponses.get(survey);
    }

    // Métodos para gestionar respuestas a exámenes de preguntas abiertas

    /**
     * Añade una respuesta a un examen de preguntas abiertas específico.
     *
     * @param exam     El examen de preguntas abiertas.
     * @param response La respuesta del estudiante.
     */
    public void addExamResponse(OpenEndedExam exam, OpenEndedResponse response) {
        examResponses.put(exam, response);
    }

    /**
     * Obtiene la respuesta de un examen de preguntas abiertas específico.
     *
     * @param exam El examen de preguntas abiertas.
     * @return La respuesta del estudiante.
     */
    public OpenEndedResponse getExamResponse(OpenEndedExam exam) {
        return examResponses.get(exam);
    }
}




