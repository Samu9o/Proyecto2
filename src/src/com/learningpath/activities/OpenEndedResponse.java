package src.com.learningpath.activities;

import src.com.learningpath.users.Student;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase que almacena las respuestas de un estudiante a un examen de preguntas abiertas.
 */
public class OpenEndedResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Student student;
    private Map<String, String> answers; // Mapea la pregunta al respuesta

    /**
     * Constructor para crear una respuesta a un examen de preguntas abiertas.
     *
     * @param student El estudiante que responde el examen.
     */
    public OpenEndedResponse(Student student) {
        this.student = student;
        this.answers = new HashMap<>();
    }

    // Getters y Setters

    /**
     * Obtiene el estudiante que respondió el examen.
     *
     * @return El estudiante.
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Establece el estudiante que respondió el examen.
     *
     * @param student El nuevo estudiante.
     */
    public void setStudent(Student student) {
        this.student = student;
    }

    /**
     * Obtiene el mapa de respuestas.
     *
     * @return El mapa que relaciona preguntas con respuestas.
     */
    public Map<String, String> getAnswers() {
        return answers;
    }

    /**
     * Añade una respuesta a una pregunta específica.
     *
     * @param question La pregunta del examen.
     * @param answer   La respuesta proporcionada por el estudiante.
     */
    public void addAnswer(String question, String answer) {
        answers.put(question, answer);
    }
}
