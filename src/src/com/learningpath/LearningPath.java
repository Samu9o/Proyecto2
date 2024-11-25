package src.com.learningpath;

import src.com.learningpath.activities.Activity;
import src.com.learningpath.activities.ActivityType;
import src.com.learningpath.users.Teacher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * La clase LearningPath representa un camino de aprendizaje creado por un profesor.
 * Contiene información detallada sobre el Learning Path, incluidas las actividades asociadas,
 * la calificación promedio, y la retroalimentación de los estudiantes.
 */
public class LearningPath implements Serializable {
    private static final long serialVersionUID = 1L;

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
    private List<Activity> activities;
    private List<String> feedbackList; // Retroalimentación de los estudiantes

    /**
     * Constructor de la clase LearningPath.
     *
     * @param title          Título del Learning Path.
     * @param description    Descripción del Learning Path.
     * @param objectives     Objetivos del Learning Path.
     * @param difficultyLevel Nivel de dificultad (1-5).
     * @param creator        Profesor que crea el Learning Path.
     */
    public LearningPath(String title, String description, String objectives, int difficultyLevel, Teacher creator) {
        this.title = title;
        this.description = description;
        this.objectives = objectives;
        setDifficultyLevel(difficultyLevel); // Validación dentro del setter
        this.creator = creator;
        this.activities = new ArrayList<>();
        this.feedbackList = new ArrayList<>();
        this.creationDate = new Date();
        this.modificationDate = new Date();
        this.version = "1.0";
        this.duration = 0;
        this.rating = 0.0;
    }

    /**
     * Agrega una actividad al Learning Path y actualiza la duración y versión.
     *
     * @param activity La actividad a agregar.
     * @return true si la actividad se agregó exitosamente, false si ya existe.
     */
    public boolean addActivity(Activity activity) {
        if (activities.contains(activity)) {
            System.out.println("La actividad '" + activity.getTitle() + "' ya existe en el Learning Path.");
            return false;
        }
        activities.add(activity);
        recalculateDuration();
        this.modificationDate = new Date();
        incrementVersion();
        return true;
    }

    /**
     * Elimina una actividad del Learning Path y actualiza la duración y versión.
     *
     * @param activity La actividad a eliminar.
     * @return true si la actividad se eliminó exitosamente, false si no existe.
     */
    
    public boolean removeActivity(Activity activity) {
        if (!activities.contains(activity)) {
            System.out.println("La actividad '" + activity.getTitle() + "' no existe en el Learning Path.");
            return false;
        }
        activities.remove(activity);
        recalculateDuration();
        this.modificationDate = new Date();
        incrementVersion();
        return true;
    }

    /**
     * Recalcula la duración total del Learning Path sumando las duraciones esperadas de cada actividad.
     */
    private void recalculateDuration() {
        this.duration = activities.stream().mapToInt(Activity::getExpectedDuration).sum();
    }

    /**
     * Incrementa la versión del Learning Path cada vez que se realiza una modificación.
     * Lógica simple de incremento de versión: "1.0" -> "1.1", "1.1" -> "1.2", etc.
     */
    private void incrementVersion() {
        String[] parts = version.split("\\.");
        int major = Integer.parseInt(parts[0]);
        int minor = Integer.parseInt(parts[1]);
        minor += 1;
        this.version = major + "." + minor;
    }

    /**
     * Actualiza la calificación promedio del Learning Path basado en las calificaciones de los estudiantes.
     *
     * @param newRating La nueva calificación proporcionada por un estudiante (0-5).
     */
    public void updateRating(double newRating) {
        if (newRating < 0.0 || newRating > 5.0) {
            System.out.println("Calificación inválida. Debe estar entre 0.0 y 5.0.");
            return;
        }
        // Implementar lógica para calcular la calificación promedio
        // Por simplicidad, asumiremos que 'rating' es un promedio acumulativo
        // En un sistema real, podrías mantener un contador de calificaciones y sumar todas para obtener el promedio
        this.rating = (this.rating + newRating) / 2;
    }

    /**
     * Agrega retroalimentación de un estudiante al Learning Path.
     *
     * @param feedback La retroalimentación proporcionada por el estudiante.
     */
    public void addFeedback(String feedback) {
        if (feedback != null && !feedback.trim().isEmpty()) {
            feedbackList.add(feedback.trim());
            this.modificationDate = new Date();
            incrementVersion();
        }
    }

    // Getters y Setters para los campos privados

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getObjectives() {
        return this.objectives;
    }

    public int getDifficultyLevel() {
        return this.difficultyLevel;
    }

    /**
     * Establece el nivel de dificultad asegurando que esté entre 1 y 5.
     *
     * @param difficultyLevel Nivel de dificultad (1-5).
     */
    public void setDifficultyLevel(int difficultyLevel) {
        if (difficultyLevel < 1 || difficultyLevel > 5) {
            throw new IllegalArgumentException("Nivel de dificultad debe estar entre 1 y 5.");
        }
        this.difficultyLevel = difficultyLevel;
    }

    public int getDuration() {
        return this.duration;
    }

    public double getRating() {
        return this.rating;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public Date getModificationDate() {
        return this.modificationDate;
    }

    public String getVersion() {
        return this.version;
    }

    public Teacher getCreator() {
        return this.creator;
    }

    public List<Activity> getActivities() {
        return this.activities;
    }

    public List<String> getFeedbackList() {
        return this.feedbackList;
    }

    /**
     * Sobrescribe el método equals basado en el título (asumiendo que es único).
     *
     * @param obj Objeto a comparar.
     * @return true si los títulos son iguales, false en caso contrario.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof LearningPath)) return false;
        LearningPath other = (LearningPath) obj;
        return title.equalsIgnoreCase(other.title);
    }

    /**
     * Sobrescribe el método hashCode basado en el título.
     *
     * @return hash code del título.
     */
    @Override
    public int hashCode() {
        return title.toLowerCase().hashCode();
    }

    /**
     * Muestra información detallada del Learning Path.
     */
    public void displayDetails() {
        System.out.println("Título: " + title);
        System.out.println("Descripción: " + description);
        System.out.println("Objetivos: " + objectives);
        System.out.println("Nivel de Dificultad: " + difficultyLevel);
        System.out.println("Duración Total: " + duration + " minutos");
        System.out.println("Calificación Promedio: " + String.format("%.2f", rating) + "/5.0");
        System.out.println("Fecha de Creación: " + creationDate);
        System.out.println("Fecha de Última Modificación: " + modificationDate);
        System.out.println("Versión: " + version);
        System.out.println("Creado por: " + creator.getName());
        System.out.println("Número de Actividades: " + activities.size());
        System.out.println("Retroalimentación de Estudiantes: " + feedbackList.size());
    }

    // Puedes añadir más métodos según tus necesidades, como obtener actividades por tipo, etc.
}

