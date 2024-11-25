	package src.com.learningpath;
	
	import src.com.learningpath.activities.Activity;
	import src.com.learningpath.activities.Assignment;
	import src.com.learningpath.activities.Quiz;
	import src.com.learningpath.activities.ResourceReview;
	import src.com.learningpath.activities.Survey;
	import src.com.learningpath.activities.OpenEndedExam;
	import src.com.learningpath.users.Teacher;
	
	import java.io.Serializable;
	import java.util.ArrayList;
	import java.util.Arrays;
	import java.util.Date;
	import java.util.HashSet;
	import java.util.List;
	import java.util.Objects;
	import java.util.Set;
	
	/**
	 * La clase LearningPath representa un camino de aprendizaje creado por un profesor.
	 * Permite gestionar actividades, feedback y versiones.
	 */
	public class LearningPath implements Serializable {
	    private static final long serialVersionUID = 1L;
	
	    // Atributos básicos del Learning Path
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
	    private List<String> feedbackList;
	
	    /**
	     * Constructor principal para crear un nuevo Learning Path.
	     *
	     * @param title          Título del Learning Path.
	     * @param description    Descripción del Learning Path.
	     * @param objectives     Objetivos del Learning Path.
	     * @param difficultyLevel Nivel de dificultad (1-5).
	     * @param creator        Profesor creador del Learning Path.
	     */
	    public LearningPath(String title, String description, String objectives, int difficultyLevel, Teacher creator) {
	        this.title = title;
	        this.description = description;
	        this.objectives = objectives;
	        this.difficultyLevel = difficultyLevel;
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
	     * Constructor de copia para crear un nuevo Learning Path basado en uno existente.
	     *
	     * @param original   El Learning Path original a copiar.
	     * @param newCreator El profesor que será el creador del nuevo Learning Path.
	     */
	    public LearningPath(LearningPath original, Teacher newCreator) {
	        this.title = original.title + " (Copia)";
	        this.description = original.description;
	        this.objectives = original.objectives;
	        this.difficultyLevel = original.difficultyLevel;
	        this.creator = newCreator;
	        this.activities = new ArrayList<>();
	        for (Activity activity : original.activities) {
	            this.activities.add(copyActivity(activity));
	        }
	        this.feedbackList = new ArrayList<>();
	        this.creationDate = new Date();
	        this.modificationDate = new Date();
	        this.version = "1.0";
	        this.duration = original.duration;
	        this.rating = original.rating;
	    }
	
	    /**
	     * Método auxiliar para copiar una actividad.
	     * Dependiendo del tipo de actividad, implementa la lógica de copia adecuada.
	     *
	     * @param activity La actividad a copiar.
	     * @return La copia de la actividad.
	     */
	    private Activity copyActivity(Activity activity) {
	        if (activity instanceof Assignment) {
	            Assignment original = (Assignment) activity;
	            return new Assignment(
	                original.getTitle(),
	                original.getDescription(),
	                original.getObjective(),
	                original.getDifficultyLevel(),
	                original.getExpectedDuration(),
	                original.isMandatory(),
	                original.getSubmissionInstructions()
	            );
	        } else if (activity instanceof Quiz) {
	            Quiz original = (Quiz) activity;
	            // Clonar las preguntas
	            List<src.com.learningpath.activities.Question> clonedQuestions = new ArrayList<>();
	            for (src.com.learningpath.activities.Question q : original.getQuestions()) {
	                clonedQuestions.add(new src.com.learningpath.activities.Question(
	                    q.getQuestionText(),
	                    q.getOptions().clone(), // Clonar el arreglo de opciones
	                    q.getCorrectOptionIndex(),
	                    q.getExplanation()
	                ));
	            }
	            return new Quiz(
	                original.getTitle(),
	                original.getDescription(),
	                original.getObjective(),
	                original.getDifficultyLevel(),
	                original.getExpectedDuration(),
	                original.isMandatory(),
	                clonedQuestions,
	                original.getPassingScore()
	            );
	        } else if (activity instanceof ResourceReview) {
	            ResourceReview original = (ResourceReview) activity;
	            return new ResourceReview(
	                original.getTitle(),
	                original.getDescription(),
	                original.getObjective(),
	                original.getDifficultyLevel(),
	                original.getExpectedDuration(),
	                original.isMandatory(),
	                original.getResourceLink()
	            );
	        } else if (activity instanceof Survey) {
	            Survey original = (Survey) activity;
	            Survey copiedSurvey = new Survey(
	                original.getTitle(),
	                original.getDescription(),
	                original.getObjective(),
	                original.getDifficultyLevel(),
	                original.getExpectedDuration(),
	                original.isMandatory()
	            );
	            for (src.com.learningpath.activities.SurveyQuestion sq : original.getSurveyQuestions()) {
	                copiedSurvey.addSurveyQuestion(new src.com.learningpath.activities.SurveyQuestion(sq.getQuestionText()));
	            }
	            return copiedSurvey;
	        } else if (activity instanceof OpenEndedExam) {
	            OpenEndedExam original = (OpenEndedExam) activity;
	            List<src.com.learningpath.activities.OpenEndedQuestion> clonedQuestions = new ArrayList<>();
	            for (src.com.learningpath.activities.OpenEndedQuestion q : original.getExamQuestions()) {
	                clonedQuestions.add(new src.com.learningpath.activities.OpenEndedQuestion(q.getQuestionText()));
	            }
	            return new OpenEndedExam(
	                original.getTitle(),
	                original.getDescription(),
	                original.getObjective(),
	                original.getDifficultyLevel(),
	                original.getExpectedDuration(),
	                original.isMandatory(),
	                new HashSet<>(Arrays.asList(src.com.learningpath.activities.ActivityType.EXAMEN)),
	                clonedQuestions
	            );
	        }
	        // Añadir más casos para otros tipos de actividades si es necesario
	        else {
	            throw new UnsupportedOperationException("Tipo de actividad no soportado para copia.");
	        }
	    }
	
	    // Métodos para gestionar actividades
	
	    /**
	     * Añade una actividad al Learning Path.
	     *
	     * @param activity La actividad a añadir.
	     * @return True si se añade exitosamente, false en caso contrario.
	     */
	    public boolean addActivity(Activity activity) {
	        if (activity != null) {
	            this.activities.add(activity);
	            this.duration += activity.getExpectedDuration();
	            this.modificationDate = new Date();
	            return true;
	        }
	        return false;
	    }
	
	    /**
	     * Elimina una actividad del Learning Path.
	     *
	     * @param activity La actividad a eliminar.
	     * @return True si se elimina exitosamente, false en caso contrario.
	     */
	    public boolean removeActivity(Activity activity) {
	        if (this.activities.remove(activity)) {
	            this.duration -= activity.getExpectedDuration();
	            this.modificationDate = new Date();
	            return true;
	        }
	        return false;
	    }
	
	    /**
	     * Muestra los detalles del Learning Path.
	     */
	    public void displayDetails() {
	        System.out.println("Título: " + title);
	        System.out.println("Descripción: " + description);
	        System.out.println("Objetivos: " + objectives);
	        System.out.println("Nivel de Dificultad: " + difficultyLevel);
	        System.out.println("Duración Total: " + duration + " minutos");
	        System.out.println("Calificación: " + rating);
	        System.out.println("Creado el: " + creationDate);
	        System.out.println("Última modificación: " + modificationDate);
	        System.out.println("Versión: " + version);
	        System.out.println("Creador: " + creator.getName());
	        System.out.println("Número de Actividades: " + activities.size());
	    }
	
	    /**
	     * Actualiza la calificación del Learning Path.
	     *
	     * @param newRating La nueva calificación.
	     */
	    public void updateRating(double newRating) {
	        this.rating = newRating;
	        this.modificationDate = new Date();
	    }
	
	    /**
	     * Añade feedback al Learning Path.
	     *
	     * @param feedback El feedback a añadir.
	     */
	    public void addFeedback(String feedback) {
	        if (feedback != null && !feedback.trim().isEmpty()) {
	            this.feedbackList.add(feedback);
	            this.modificationDate = new Date();
	        }
	    }
	
	    // Getters
	
	    public String getTitle() {
	        return title;
	    }
	
	    public String getDescription() {
	        return description;
	    }
	
	    public String getObjectives() {
	        return objectives;
	    }
	
	    public int getDifficultyLevel() {
	        return difficultyLevel;
	    }
	
	    public int getDuration() {
	        return duration;
	    }
	
	    public double getRating() {
	        return rating;
	    }
	
	    public Date getCreationDate() {
	        return creationDate;
	    }
	
	    public Date getModificationDate() {
	        return modificationDate;
	    }
	
	    public String getVersion() {
	        return version;
	    }
	
	    public Teacher getCreator() {
	        return creator;
	    }
	
	    public List<Activity> getActivities() {
	        return activities;
	    }
	
	    public List<String> getFeedbackList() {
	        return feedbackList;
	    }
	
	    // Sobrescribir equals y hashCode basados en title y creator (asumiendo que juntos son únicos)
	
	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	
	        LearningPath that = (LearningPath) o;
	
	        return Objects.equals(title, that.title) &&
	               Objects.equals(creator, that.creator);
	    }
	
	    @Override
	    public int hashCode() {
	        return Objects.hash(title, creator);
	    }
	}

