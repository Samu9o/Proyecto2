package src.com.learningpath.activities;



import java.io.Serializable;

/**
 * Clase que representa una pregunta abierta en un examen.
 */
public class OpenEndedQuestion implements Serializable {
    private static final long serialVersionUID = 1L;

    private String questionText;
    private String studentAnswer; // Respuesta del estudiante
    private String professorFeedback; // Retroalimentación del profesor
    private double grade; // Calificación otorgada por el profesoreeeeee

    /**
     * Constructor de la clase OpenEndedQuestion.
     *
     * @param questionText Texto de la pregunta.
     */
    public OpenEndedQuestion(String questionText) {
        this.questionText = questionText;
        this.studentAnswer = "";
        this.professorFeedback = "";
        this.grade = 0.0;
    }

    // Getters y Setters

    public String getQuestionText() {
        return questionText;
    }

    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public String getProfessorFeedback() {
        return professorFeedback;
    }

    public void setProfessorFeedback(String professorFeedback) {
        this.professorFeedback = professorFeedback;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}

