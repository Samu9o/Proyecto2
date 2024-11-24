package src.com.learningpath;

import src.com.learningpath.activities.Activity;
import src.com.learningpath.users.Teacher;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        this.difficultyLevel = difficultyLevel;
        this.creator = creator;
        this.activities = new ArrayList<>();
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
     */
    public void addActivity(Activity activity) {
        activities.add(activity);
        recalculateDuration();
        this.modificationDate = new Date();
        incrementVersion();
    }

    /**
     * Recalcula la duración total del Learning Path sumando las duraciones esperadas de cada actividad.
     */
    private void recalculateDuration() {
        this.duration = activities.stream().mapToInt(Activity::getExpectedDuration).sum();
    }

    /**
     * Incrementa la versión del Learning Path cada vez que se realiza una modificación.
     */
    private void incrementVersion() {
        // Lógica simple de incremento de versión: "1.0" -> "1.1", "1.1" -> "1.2", etc.
        String[] parts = version.split("\\.");
        int major = Integer.parseInt(parts[0]);
        int minor = Integer.parseInt(parts[1]);
        minor += 1;
        this.version = major + "." + minor;
    }

    // Getters para los campos privados

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

    // Opcional: setters si necesitas modificar los campos después de la creación
    // Aunque es recomendable mantener los campos inmutables si no es necesario cambiarlos.

    // Sobrescribimos equals y hashCode basados en el título (asumiendo que es único)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof LearningPath)) return false;
        LearningPath other = (LearningPath) obj;
        return title.equals(other.title);
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }
}

