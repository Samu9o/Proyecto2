package src.com.learningpath.activities;

/**
 * Enumeración que define los diferentes estados posibles de una actividad.
 */
public enum ActivityStatus {
    PENDING,     // Pendiente
    COMPLETED,   // Completada
    SUBMITTED,   // Entregada (para actividades que requieren revisión)
    FAILED       // Fallida (para actividades evaluables como quizzes)
}
