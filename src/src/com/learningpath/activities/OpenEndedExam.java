package src.com.learningpath.activities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Clase que representa un examen de preguntas abiertas.
 */
public class OpenEndedExam extends Activity implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<OpenEndedQuestion> examQuestions;
    private List<OpenEndedResponse> examResponses;

    /**
     * Constructor para crear un examen de preguntas abiertas.
     *
     * @param title            Título del examen.
     * @param description      Descripción del examen.
     * @param objective        Objetivo del examen.
     * @param difficultyLevel  Nivel de dificultad (1-5).
     * @param expectedDuration Duración esperada en minutos.
     * @param isMandatory      Indica si el examen es obligatorio.
     * @param types            Conjunto de tipos de actividades relacionadas.
     * @param openEndedQuestions Lista de preguntas abiertas del examen.
     */
    public OpenEndedExam(String title, String description, String objective, int difficultyLevel,
                        int expectedDuration, boolean isMandatory, Set<ActivityType> types,
                        List<OpenEndedQuestion> openEndedQuestions) {
        super(title, description, objective, difficultyLevel, expectedDuration, isMandatory);
        this.examQuestions = openEndedQuestions;
        this.examResponses = new ArrayList<>();
    }

    // Getters y Setters

    /**
     * Obtiene la lista de preguntas del examen.
     *
     * @return La lista de preguntas.
     */
    public List<OpenEndedQuestion> getExamQuestions() {
        return examQuestions;
    }

    /**
     * Añade una pregunta al examen.
     *
     * @param question La pregunta a añadir.
     */
    public void addExamQuestion(OpenEndedQuestion question) {
        examQuestions.add(question);
    }

    /**
     * Obtiene la lista de respuestas al examen.
     *
     * @return La lista de respuestas.
     */
    public List<OpenEndedResponse> getExamResponses() {
        return examResponses;
    }

    /**
     * Añade una respuesta al examen.
     *
     * @param response La respuesta a añadir.
     */
    public void addExamResponse(OpenEndedResponse response) {
        examResponses.add(response);
    }

    @Override
    public String getType() {
        return "OpenEndedExam";
    }

    // Implementación de hashCode y equals si es necesario
}
