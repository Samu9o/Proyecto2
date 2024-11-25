package src.com.learningpath.activities;



import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Clase que representa un examen con preguntas abiertas.
 */
public class Examen extends Activity {
    private static final long serialVersionUID = 1L;

    private List<OpenEndedQuestion> openEndedQuestions;

    /**
     * Constructor de la clase Examen.
     *
     * @param title        Título del examen.
     * @param description  Descripción del examen.
     * @param objective    Objetivo del examen.
     * @param difficultyLevel Nivel de dificultad (1-5).
     * @param expectedDuration Duración esperada en minutos.
     * @param isMandatory  Indica si el examen es obligatorio.
     * @param types        Conjunto de tipos de actividad.
     * @param openEndedQuestions Lista de preguntas abiertas.
     */
    public Examen(String title, String description, String objective, int difficultyLevel,
                 int expectedDuration, boolean isMandatory, Set<ActivityType> types,
                 List<OpenEndedQuestion> openEndedQuestions) {
        super(title, description, objective, difficultyLevel, expectedDuration, isMandatory, types);
        this.openEndedQuestions = openEndedQuestions;
    }

    public List<OpenEndedQuestion> getOpenEndedQuestions() {
        return openEndedQuestions;
    }

    public void addOpenEndedQuestion(OpenEndedQuestion question) {
        this.openEndedQuestions.add(question);
    }

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

    // Puedes añadir más métodos específicos de Examen si lo deseas
}
