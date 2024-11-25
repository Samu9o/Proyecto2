package src.com.learningpath.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

import src.com.learningpath.LearningPath;
import src.com.learningpath.Progress;
import src.com.learningpath.activities.Activity;
import src.com.learningpath.activities.ActivityStatus;
import src.com.learningpath.activities.ActivityType;
import src.com.learningpath.activities.Assignment;
import src.com.learningpath.activities.OpenEndedExam;
import src.com.learningpath.activities.OpenEndedQuestion;
import src.com.learningpath.activities.OpenEndedResponse;
import src.com.learningpath.activities.Question;
import src.com.learningpath.activities.Quiz;
import src.com.learningpath.activities.ResourceReview;
import src.com.learningpath.activities.Survey;
import src.com.learningpath.activities.SurveyQuestion;
import src.com.learningpath.activities.SurveyResponse;
import src.com.learningpath.data.DataManager;
import src.com.learningpath.users.Student;
import src.com.learningpath.users.Teacher;
import src.com.learningpath.users.User;

/**
 * Clase que gestiona la interfaz de consola para la aplicación de Learning Paths.
 */
public class ConsoleInterface {
    private Scanner scanner;
    private List<User> users;
    private List<LearningPath> learningPaths;
    private List<Progress> progresses;
    private User currentUser;

    /**
     * Constructor de la clase ConsoleInterface.
     * Inicializa los componentes y carga los datos.
     */
    public ConsoleInterface() {
        scanner = new Scanner(System.in);
        // Cargar datos
        try {
            users = DataManager.loadUsers();
            learningPaths = DataManager.loadLearningPaths();
            progresses = DataManager.loadProgresses();
        } catch (Exception e) {
            users = new ArrayList<>();
            learningPaths = new ArrayList<>();
            progresses = new ArrayList<>();
            System.out.println("No se encontraron datos previos. Se iniciará con datos vacíos.");
        }

        // Registrar el shutdown hook para guardar datos al cerrar la aplicación
        Runtime.getRuntime().addShutdownHook(new Thread(this::saveData));
    }

    /**
     * Método principal que inicia la interfaz de consola.
     */
    public void start() {
        System.out.println("=== Sistema de Gestión de Learning Paths ===");
        boolean exit = false;
        while (!exit) {
            if (currentUser == null) {
                System.out.println("\n1. Iniciar sesión");
                System.out.println("2. Registrarse");
                System.out.println("3. Salir");
                System.out.print("Seleccione una opción: ");
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1":
                        login();
                        break;
                    case "2":
                        register();
                        break;
                    case "3":
                        exit = true;
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } else {
                if (currentUser instanceof Teacher) {
                    teacherMenu();
                } else if (currentUser instanceof Student) {
                    studentMenu();
                }
            }
        }
        // Guardar datos antes de salir de forma normal
        saveData();
        System.out.println("Hasta luego.");
    }

    /**
     * Guarda todos los datos utilizando DataManager.
     */
    private void saveData() {
        try {
            DataManager.saveUsers(users);
            DataManager.saveLearningPaths(learningPaths);
            DataManager.saveProgresses(progresses);
            System.out.println("Datos guardados exitosamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar datos: " + e.getMessage());
        }
    }

    /**
     * Permite al usuario iniciar sesión en el sistema.
     */
    private void login() {
        System.out.print("Nombre de usuario: ");
        String username = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();
        Optional<User> userOpt = users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
        if (userOpt.isPresent() && userOpt.get().authenticate(password)) {
            currentUser = userOpt.get();
            System.out.println("Bienvenido, " + currentUser.getName() + " (" + currentUser.getRole() + ")");
        } else {
            System.out.println("Credenciales incorrectas.");
        }
    }

    /**
     * Permite al usuario registrarse en el sistema seleccionando su rol.
     */
    private void register() {
        System.out.print("Nombre de usuario: ");
        String username = scanner.nextLine();
        if (users.stream().anyMatch(u -> u.getUsername().equals(username))) {
            System.out.println("El nombre de usuario ya existe. Por favor, elija otro.");
            return;
        }
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();
        System.out.print("Confirmar contraseña: ");
        String confirmPassword = scanner.nextLine();
        if (!password.equals(confirmPassword)) {
            System.out.println("Las contraseñas no coinciden.");
            return;
        }
        System.out.print("Nombre completo: ");
        String name = scanner.nextLine();
        System.out.print("Rol (1-Estudiante, 2-Profesor): ");
        String roleChoice = scanner.nextLine();
        User newUser;
        if (roleChoice.equals("1")) {
            newUser = new Student(username, password, name);
        } else if (roleChoice.equals("2")) {
            newUser = new Teacher(username, password, name);
        } else {
            System.out.println("Rol no válido.");
            return;
        }
        users.add(newUser);
        // Guardar datos inmediatamente después de registrar un nuevo usuario
        saveData();
        System.out.println("Usuario registrado exitosamente. Ahora puede iniciar sesión.");
    }

    /**
     * Menú específico para profesores.
     */
    private void teacherMenu() {
        Teacher teacher = (Teacher) currentUser;
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Menú de Profesor ===");
            System.out.println("1. Crear Learning Path");
            System.out.println("2. Ver Mis Learning Paths");
            System.out.println("3. Ver Learning Paths de Otros Profesores");
            System.out.println("4. Copiar un Learning Path");
            System.out.println("5. Ver Estudiantes Inscritos");
            System.out.println("6. Ver Respuestas a Encuestas");
            System.out.println("7. Ver Respuestas a Exámenes de Preguntas Abiertas");
            System.out.println("8. Cerrar sesión");
            System.out.print("Seleccione una opción: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    createLearningPath(teacher);
                    break;
                case "2":
                    viewLearningPaths(teacher);
                    break;
                case "3":
                    viewAllLearningPaths(teacher);
                    break;
                case "4":
                    copyLearningPath(teacher);
                    break;
                case "5":
                    viewEnrolledStudents(teacher);
                    break;
                case "6":
                    viewSurveyResponses(teacher);
                    break;
                case "7":
                    viewOpenEndedExamResponses(teacher);
                    break;
                case "8":
                    currentUser = null;
                    back = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    /**
     * Permite al profesor crear un nuevo Learning Path.
     *
     * @param teacher El profesor que está creando el Learning Path.
     */
    private void createLearningPath(Teacher teacher) {
        System.out.println("\n=== Crear Nuevo Learning Path ===");
        System.out.print("Título: ");
        String title = scanner.nextLine();
        System.out.print("Descripción: ");
        String description = scanner.nextLine();
        System.out.print("Objetivos: ");
        String objectives = scanner.nextLine();
        int difficultyLevel = readIntegerInput("Nivel de dificultad (1-5): ", 1, 5);
        
        LearningPath newLP = new LearningPath(title, description, objectives, difficultyLevel, teacher);
        
        boolean addingActivities = true;
        while (addingActivities) {
            System.out.println("\nAñadir una actividad:");
            System.out.println("1. Assignment");
            System.out.println("2. Quiz");
            System.out.println("3. Resource Review");
            System.out.println("4. Survey");
            System.out.println("5. Open-Ended Exam");
            System.out.println("6. Finalizar y guardar Learning Path");
            System.out.print("Seleccione el tipo de actividad a añadir: ");
            String activityChoice = scanner.nextLine();
            switch (activityChoice) {
                case "1":
                    Activity assignment = createAssignment();
                    if (assignment != null) {
                        newLP.addActivity(assignment);
                        System.out.println("Assignment añadido exitosamente.");
                    }
                    break;
                case "2":
                    Activity quiz = createQuiz();
                    if (quiz != null) {
                        newLP.addActivity(quiz);
                        System.out.println("Quiz añadido exitosamente.");
                    }
                    break;
                case "3":
                    Activity resourceReview = createResourceReview();
                    if (resourceReview != null) {
                        newLP.addActivity(resourceReview);
                        System.out.println("Resource Review añadido exitosamente.");
                    }
                    break;
                case "4":
                    Activity survey = createSurvey();
                    if (survey != null) {
                        newLP.addActivity(survey);
                        System.out.println("Survey añadido exitosamente.");
                    }
                    break;
                case "5":
                    Activity openEndedExam = createOpenEndedExam();
                    if (openEndedExam != null) {
                        newLP.addActivity(openEndedExam);
                        System.out.println("Open-Ended Exam añadido exitosamente.");
                    }
                    break;
                case "6":
                    addingActivities = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }

        learningPaths.add(newLP);
        // Guardar datos después de crear un Learning Path
        saveData();
        System.out.println("Learning Path creado exitosamente.");
    }
    /**
     * Crea una nueva instancia de Assignment basada en la entrada del usuario.
     *
     * @return La instancia de Assignment creada.
     */
    private Assignment createAssignment() {
        System.out.println("\n=== Crear Assignment ===");
        System.out.print("Título: ");
        String title = scanner.nextLine();
        System.out.print("Descripción: ");
        String description = scanner.nextLine();
        System.out.print("Objetivo: ");
        String objective = scanner.nextLine();
        int difficultyLevel = readIntegerInput("Nivel de dificultad (1-5): ", 1, 5);
        int expectedDuration = readIntegerInput("Duración esperada en minutos: ", 1, 1440);
        boolean isMandatory = readBooleanInput("¿Es obligatorio? (s/n): ");
        System.out.print("Instrucciones de entrega: ");
        String submissionInstructions = scanner.nextLine();

        return new Assignment(title, description, objective, difficultyLevel, expectedDuration, isMandatory, submissionInstructions);
    }
    /**
     * Crea una nueva instancia de Quiz basada en la entrada del usuario.
     *
     * @return La instancia de Quiz creada.
     */
    private Quiz createQuiz() {
        System.out.println("\n=== Crear Quiz ===");
        System.out.print("Título: ");
        String title = scanner.nextLine();
        System.out.print("Descripción: ");
        String description = scanner.nextLine();
        System.out.print("Objetivo: ");
        String objective = scanner.nextLine();
        int difficultyLevel = readIntegerInput("Nivel de dificultad (1-5): ", 1, 5);
        int expectedDuration = readIntegerInput("Duración esperada en minutos: ", 1, 1440);
        boolean isMandatory = readBooleanInput("¿Es obligatorio? (s/n): ");
        double passingScore = readDoubleInput("Puntuación mínima para aprobar (%): ", 0.0, 100.0);

        List<Question> questions = new ArrayList<>();
        boolean addingQuestions = true;
        while (addingQuestions) {
            System.out.println("\nAñadir una pregunta al Quiz:");
            System.out.print("Texto de la pregunta: ");
            String questionText = scanner.nextLine();
            String[] options = new String[4];
            for (int i = 0; i < 4; i++) {
                System.out.print("Opción " + (i + 1) + ": ");
                options[i] = scanner.nextLine();
            }
            int correctOptionIndex = readIntegerInput("Índice de la opción correcta (1-4): ", 1, 4) - 1;
            System.out.print("Explicación de la respuesta: ");
            String explanation = scanner.nextLine();
            questions.add(new Question(questionText, options, correctOptionIndex, explanation));

            System.out.print("¿Añadir otra pregunta? (s/n): ");
            String continueChoice = scanner.nextLine();
            if (!continueChoice.equalsIgnoreCase("s")) {
                addingQuestions = false;
            }
        }

        return new Quiz(title, description, objective, difficultyLevel, expectedDuration, isMandatory, questions, passingScore);
    }

    /**
     * Crea una nueva instancia de ResourceReview basada en la entrada del usuario.
     *
     * @return La instancia de ResourceReview creada.
     */
    private ResourceReview createResourceReview() {
        System.out.println("\n=== Crear Resource Review ===");
        System.out.print("Título: ");
        String title = scanner.nextLine();
        System.out.print("Descripción: ");
        String description = scanner.nextLine();
        System.out.print("Objetivo: ");
        String objective = scanner.nextLine();
        int difficultyLevel = readIntegerInput("Nivel de dificultad (1-5): ", 1, 5);
        int expectedDuration = readIntegerInput("Duración esperada en minutos: ", 1, 1440);
        boolean isMandatory = readBooleanInput("¿Es obligatorio? (s/n): ");
        System.out.print("Enlace al recurso: ");
        String resourceLink = scanner.nextLine();

        return new ResourceReview(title, description, objective, difficultyLevel, expectedDuration, isMandatory, resourceLink);
    }
    /**
     * Crea una nueva instancia de Survey basada en la entrada del usuario.
     *
     * @return La instancia de Survey creada.
     */
    private Survey createSurvey() {
        System.out.println("\n=== Crear Survey ===");
        System.out.print("Título: ");
        String title = scanner.nextLine();
        System.out.print("Descripción: ");
        String description = scanner.nextLine();
        System.out.print("Objetivo: ");
        String objective = scanner.nextLine();
        int difficultyLevel = readIntegerInput("Nivel de dificultad (1-5): ", 1, 5);
        int expectedDuration = readIntegerInput("Duración esperada en minutos: ", 1, 1440);
        boolean isMandatory = readBooleanInput("¿Es obligatorio? (s/n): ");

        Survey survey = new Survey(title, description, objective, difficultyLevel, expectedDuration, isMandatory);

        boolean addingQuestions = true;
        while (addingQuestions) {
            System.out.println("\nAñadir una pregunta al Survey:");
            System.out.print("Texto de la pregunta: ");
            String questionText = scanner.nextLine();
            survey.addSurveyQuestion(new SurveyQuestion(questionText));

            System.out.print("¿Añadir otra pregunta? (s/n): ");
            String continueChoice = scanner.nextLine();
            if (!continueChoice.equalsIgnoreCase("s")) {
                addingQuestions = false;
            }
        }

        return survey;
    }
    /**
     * Crea una nueva instancia de OpenEndedExam basada en la entrada del usuario.
     *
     * @return La instancia de OpenEndedExam creada.
     */
    private OpenEndedExam createOpenEndedExam() {
        System.out.println("\n=== Crear Open-Ended Exam ===");
        System.out.print("Título: ");
        String title = scanner.nextLine();
        System.out.print("Descripción: ");
        String description = scanner.nextLine();
        System.out.print("Objetivo: ");
        String objective = scanner.nextLine();
        int difficultyLevel = readIntegerInput("Nivel de dificultad (1-5): ", 1, 5);
        int expectedDuration = readIntegerInput("Duración esperada en minutos: ", 1, 1440);
        boolean isMandatory = readBooleanInput("¿Es obligatorio? (s/n): ");

        Set<ActivityType> types = new HashSet<>(Arrays.asList(ActivityType.EXAMEN));

        List<OpenEndedQuestion> openEndedQuestions = new ArrayList<>();
        boolean addingQuestions = true;
        while (addingQuestions) {
            System.out.println("\nAñadir una pregunta al Open-Ended Exam:");
            System.out.print("Texto de la pregunta: ");
            String questionText = scanner.nextLine();
            openEndedQuestions.add(new OpenEndedQuestion(questionText));

            System.out.print("¿Añadir otra pregunta? (s/n): ");
            String continueChoice = scanner.nextLine();
            if (!continueChoice.equalsIgnoreCase("s")) {
                addingQuestions = false;
            }
        }

        return new OpenEndedExam(title, description, objective, difficultyLevel, expectedDuration, isMandatory, types, openEndedQuestions);
    }


    /**
     * Permite al profesor ver todos sus Learning Paths.
     *
     * @param teacher El profesor que está visualizando sus Learning Paths.
     */
    private void viewLearningPaths(Teacher teacher) {
        // Filtrar los Learning Paths creados por el profesor actual
        List<LearningPath> teacherLPs = new ArrayList<>();
        for (LearningPath lp : learningPaths) {
            if (lp.getCreator().equals(teacher)) {
                teacherLPs.add(lp);
            }
        }

        // Verificar si el profesor tiene Learning Paths creados
        if (teacherLPs.isEmpty()) {
            System.out.println("No tiene Learning Paths creados.");
            return;
        }

        // Mostrar los Learning Paths del profesor
        System.out.println("\n=== Sus Learning Paths ===");
        for (int i = 0; i < teacherLPs.size(); i++) {
            LearningPath lp = teacherLPs.get(i);
            System.out.println((i + 1) + ". " + lp.getTitle());
            System.out.println("   Descripción: " + lp.getDescription());
            System.out.println("   Objetivos: " + lp.getObjectives());
            System.out.println("   Nivel de Dificultad: " + lp.getDifficultyLevel());
            System.out.println("   Duración Total: " + lp.getDuration() + " minutos");
            System.out.println("   Versión: " + lp.getVersion());
        }
    }

    /**
     * Permite al profesor ver todos los Learning Paths creados por otros profesores.
     *
     * @param teacher El profesor que está visualizando los Learning Paths.
     */
    private void viewAllLearningPaths(Teacher teacher) {
        List<LearningPath> otherLearningPaths = new ArrayList<>();
        for (LearningPath lp : learningPaths) {
            if (!lp.getCreator().equals(teacher)) {
                otherLearningPaths.add(lp);
            }
        }

        if (otherLearningPaths.isEmpty()) {
            System.out.println("No hay Learning Paths creados por otros profesores.");
            return;
        }

        System.out.println("\n=== Learning Paths de Otros Profesores ===");
        for (int i = 0; i < otherLearningPaths.size(); i++) {
            LearningPath lp = otherLearningPaths.get(i);
            System.out.println((i + 1) + ". " + lp.getTitle() + " (Creado por: " + lp.getCreator().getName() + ")");
            System.out.println("   Descripción: " + lp.getDescription());
            System.out.println("   Objetivos: " + lp.getObjectives());
            System.out.println("   Nivel de Dificultad: " + lp.getDifficultyLevel());
            System.out.println("   Duración Total: " + lp.getDuration() + " minutos");
            System.out.println("   Versión: " + lp.getVersion());
        }
    }

    /**
     * Permite al profesor copiar un Learning Path existente de otro profesor.
     *
     * @param teacher El profesor que está realizando la copia.
     */
    private void copyLearningPath(Teacher teacher) {
        List<LearningPath> otherLearningPaths = new ArrayList<>();
        for (LearningPath lp : learningPaths) {
            if (!lp.getCreator().equals(teacher)) {
                otherLearningPaths.add(lp);
            }
        }

        if (otherLearningPaths.isEmpty()) {
            System.out.println("No hay Learning Paths creados por otros profesores para copiar.");
            return;
        }

        System.out.println("\n=== Learning Paths Disponibles para Copiar ===");
        for (int i = 0; i < otherLearningPaths.size(); i++) {
            LearningPath lp = otherLearningPaths.get(i);
            System.out.println((i + 1) + ". " + lp.getTitle() + " (Creado por: " + lp.getCreator().getName() + ")");
        }

        int choice = readIntegerInput("Seleccione un Learning Path para copiar (0 para regresar): ", 0, otherLearningPaths.size());
        if (choice == 0) {
            return;
        }

        LearningPath selectedLP = otherLearningPaths.get(choice - 1);
        try {
            LearningPath copiedLP = new LearningPath(selectedLP, teacher);
            learningPaths.add(copiedLP);

            // Guardar datos después de copiar
            saveData();

            System.out.println("Learning Path copiado exitosamente como: " + copiedLP.getTitle());
        } catch (UnsupportedOperationException e) {
            System.out.println("Error al copiar el Learning Path: " + e.getMessage());
        }
    }

    /**
     * Permite al profesor ver los estudiantes inscritos en sus Learning Paths.
     *
     * @param teacher El profesor que está visualizando los estudiantes inscritos.
     */
    private void viewEnrolledStudents(Teacher teacher) {
        // Obtener los Learning Paths creados por el profesor
        List<LearningPath> teacherLPs = new ArrayList<>();
        for (LearningPath lp : learningPaths) {
            if (lp.getCreator().equals(teacher)) {
                teacherLPs.add(lp);
            }
        }

        if (teacherLPs.isEmpty()) {
            System.out.println("No tiene Learning Paths creados.");
            return;
        }

        System.out.println("\n=== Estudiantes Inscritos en sus Learning Paths ===");
        for (LearningPath lp : teacherLPs) {
            System.out.println("\nLearning Path: " + lp.getTitle());
            boolean hasStudents = false;
            for (Progress p : progresses) {
                if (p.getLearningPath().equals(lp)) {
                    System.out.println("- Estudiante: " + p.getStudent().getName());
                    hasStudents = true;
                }
            }
            if (!hasStudents) {
                System.out.println("  No hay estudiantes inscritos.");
            }
        }
    }

    /**
     * Permite al profesor ver las respuestas a encuestas en sus Learning Paths.
     *
     * @param teacher El profesor que está revisando las respuestas a encuestas.
     */
    private void viewSurveyResponses(Teacher teacher) {
        // Obtener los Learning Paths creados por el profesor
        List<LearningPath> teacherLPs = new ArrayList<>();
        for (LearningPath lp : learningPaths) {
            if (lp.getCreator().equals(teacher)) {
                teacherLPs.add(lp);
            }
        }

        if (teacherLPs.isEmpty()) {
            System.out.println("No tiene Learning Paths creados.");
            return;
        }

        for (LearningPath lp : teacherLPs) {
            System.out.println("\n=== Respuestas a Encuestas en Learning Path: " + lp.getTitle() + " ===");
            for (Activity activity : lp.getActivities()) {
                if (activity instanceof Survey) {
                    Survey survey = (Survey) activity;
                    List<SurveyResponse> responses = survey.getSurveyResponses();
                    if (responses.isEmpty()) {
                        System.out.println("  No hay respuestas para la encuesta: " + survey.getTitle());
                        continue;
                    }
                    System.out.println("\n  Encuesta: " + survey.getTitle());
                    for (SurveyResponse response : responses) {
                        System.out.println("    Estudiante: " + response.getStudent().getName());
                        List<String> answers = response.getAnswers();
                        List<SurveyQuestion> questions = survey.getSurveyQuestions();
                        for (int i = 0; i < questions.size(); i++) {
                            System.out.println("      Pregunta " + (i + 1) + ": " + questions.get(i).getQuestionText());
                            System.out.println("      Respuesta: " + (i < answers.size() ? answers.get(i) : "No respondida"));
                        }
                    }
                }
            }
        }
    }

    /**
     * Permite al profesor ver las respuestas a exámenes de preguntas abiertas en sus Learning Paths.
     *
     * @param teacher El profesor que está revisando las respuestas a exámenes.
     */
    private void viewOpenEndedExamResponses(Teacher teacher) {
        // Obtener los Learning Paths creados por el profesor
        List<LearningPath> teacherLPs = new ArrayList<>();
        for (LearningPath lp : learningPaths) {
            if (lp.getCreator().equals(teacher)) {
                teacherLPs.add(lp);
            }
        }

        if (teacherLPs.isEmpty()) {
            System.out.println("No tiene Learning Paths creados.");
            return;
        }

        for (LearningPath lp : teacherLPs) {
            System.out.println("\n=== Respuestas a Exámenes de Preguntas Abiertas en Learning Path: " + lp.getTitle() + " ===");
            for (Activity activity : lp.getActivities()) {
                if (activity instanceof OpenEndedExam) {
                    OpenEndedExam exam = (OpenEndedExam) activity;
                    List<OpenEndedResponse> responses = exam.getExamResponses();
                    if (responses.isEmpty()) {
                        System.out.println("  No hay respuestas para el examen: " + exam.getTitle());
                        continue;
                    }
                    System.out.println("\n  Examen: " + exam.getTitle());
                    for (OpenEndedResponse response : responses) {
                        System.out.println("    Estudiante: " + response.getStudent().getName());
                        Map<String, String> answers = response.getAnswers();
                        for (Map.Entry<String, String> entry : answers.entrySet()) {
                            System.out.println("      Pregunta: " + entry.getKey());
                            System.out.println("      Respuesta: " + entry.getValue());
                        }
                    }
                }
            }
        }
    }

    /**
     * Menú específico para estudiantes.
     */
    private void studentMenu() {
        Student student = (Student) currentUser;
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Menú de Estudiante ===");
            System.out.println("1. Ver Learning Paths disponibles");
            System.out.println("2. Ver mis Learning Paths");
            System.out.println("3. Cerrar sesión");
            System.out.print("Seleccione una opción: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    enrollInLearningPath(student);
                    break;
                case "2":
                    viewMyLearningPaths(student);
                    break;
                case "3":
                    currentUser = null;
                    back = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    /**
     * Permite al estudiante inscribirse en un Learning Path disponible.
     *
     * @param student El estudiante que se está inscribiendo.
     */
    private void enrollInLearningPath(Student student) {
        List<LearningPath> availableLPs = new ArrayList<>(learningPaths);
        // Excluir Learning Paths en los que ya está inscrito
        for (Progress p : progresses) {
            if (p.getStudent().equals(student)) {
                availableLPs.remove(p.getLearningPath());
            }
        }
        if (availableLPs.isEmpty()) {
            System.out.println("No hay Learning Paths disponibles para inscribirse.");
            return;
        }
        System.out.println("\n=== Learning Paths Disponibles ===");
        for (int i = 0; i < availableLPs.size(); i++) {
            LearningPath lp = availableLPs.get(i);
            System.out.println((i + 1) + ". " + lp.getTitle() + " (Creado por: " + lp.getCreator().getName() + ")");
            System.out.println("   Descripción: " + lp.getDescription());
            System.out.println("   Objetivos: " + lp.getObjectives());
            System.out.println("   Nivel de Dificultad: " + lp.getDifficultyLevel());
            System.out.println("   Duración Total: " + lp.getDuration() + " minutos");
            System.out.println("   Versión: " + lp.getVersion());
        }
        int choice = readIntegerInput("Seleccione un Learning Path para inscribirse (0 para regresar): ", 0, availableLPs.size());
        if (choice == 0) {
            return;
        }
        LearningPath selectedLP = availableLPs.get(choice - 1);
        Progress progress = new Progress(student, selectedLP);
        progresses.add(progress);
        // Guardar datos
        saveData();
        System.out.println("Inscrito en " + selectedLP.getTitle());
    }

    /**
     * Permite al estudiante ver y gestionar sus Learning Paths inscritos.
     *
     * @param student El estudiante cuyo progreso se está visualizando.
     */
    private void viewMyLearningPaths(Student student) {
        List<Progress> myProgresses = new ArrayList<>();
        for (Progress p : progresses) {
            if (p.getStudent().equals(student)) {
                myProgresses.add(p);
            }
        }
        if (myProgresses.isEmpty()) {
            System.out.println("No está inscrito en ningún Learning Path.");
            return;
        }
        System.out.println("\n=== Mis Learning Paths ===");
        for (int i = 0; i < myProgresses.size(); i++) {
            Progress p = myProgresses.get(i);
            System.out.println((i + 1) + ". " + p.getLearningPath().getTitle() + " - " + String.format("%.2f", p.calculateCompletionPercentage()) + "% completado");
        }
        int choice = readIntegerInput("Seleccione un Learning Path para ver detalles (0 para regresar): ", 0, myProgresses.size());
        if (choice == 0) {
            return;
        }
        Progress selectedProgress = myProgresses.get(choice - 1);
        interactWithLearningPath(selectedProgress);
    }

    /**
     * Permite al estudiante interactuar con un Learning Path, realizando actividades.
     *
     * @param progress El progreso del Learning Path seleccionado.
     */
    private void interactWithLearningPath(Progress progress) {
        System.out.println("\n=== Actividades en " + progress.getLearningPath().getTitle() + " ===");
        List<Activity> activities = progress.getLearningPath().getActivities();
        for (int i = 0; i < activities.size(); i++) {
            Activity activity = activities.get(i);
            ActivityStatus status = progress.getActivityStatus(activity);
            System.out.println((i + 1) + ". " + activity.getTitle() + " - " + status + " - Tipo: " + activity.getType());
        }
        int choice = readIntegerInput("Seleccione una actividad para realizar (0 para regresar): ", 0, activities.size());
        if (choice == 0) {
            return;
        }
        Activity selectedActivity = activities.get(choice - 1);
        ActivityStatus status = progress.getActivityStatus(selectedActivity);
        if (status == ActivityStatus.COMPLETED || status == ActivityStatus.SUBMITTED) {
            System.out.println("Esta actividad ya ha sido completada.");
            return;
        }
        switch (selectedActivity.getType()) {
            case "Survey":
                respondToSurvey(progress, (Survey) selectedActivity);
                break;
            case "OpenEndedExam":
                respondToOpenEndedExam(progress, (OpenEndedExam) selectedActivity);
                break;
            default:
                performActivity(progress, selectedActivity);
        }
    }

    /**
     * Permite al estudiante responder una encuesta.
     *
     * @param progress El progreso del Learning Path.
     * @param survey   La encuesta a la que se responderá.
     */
    private void respondToSurvey(Progress progress, Survey survey) {
        // Verificar si el estudiante ya ha respondido la encuesta
        SurveyResponse existingResponse = progress.getSurveyResponse(survey);
        if (existingResponse != null) {
            System.out.println("Ya has respondido a esta encuesta.");
            return;
        }

        SurveyResponse response = new SurveyResponse((Student) currentUser);
        System.out.println("\n=== Respondida a la Encuesta: " + survey.getTitle() + " ===");
        for (SurveyQuestion question : survey.getSurveyQuestions()) {
            System.out.println("Pregunta: " + question.getQuestionText());
            System.out.print("Tu respuesta: ");
            String answer = scanner.nextLine();
            response.addAnswer(answer);
        }

        // Añadir la respuesta al progreso
        progress.addSurveyResponse(survey, response);
        survey.addSurveyResponse(response);

        // Marcar la encuesta como completada
        progress.updateActivityStatus(survey, ActivityStatus.COMPLETED);

        // Guardar datos
        saveData();
        System.out.println("Gracias por responder la encuesta.");
    }

    /**
     * Permite al estudiante responder un examen de preguntas abiertas.
     *
     * @param progress El progreso del Learning Path.
     * @param exam     El examen a responder.
     */
    private void respondToOpenEndedExam(Progress progress, OpenEndedExam exam) {
        // Verificar si el estudiante ya ha respondido el examen
        OpenEndedResponse existingResponse = progress.getExamResponse(exam);
        if (existingResponse != null) {
            System.out.println("Ya has respondido a este examen.");
            return;
        }

        OpenEndedResponse response = new OpenEndedResponse((Student) currentUser);
        System.out.println("\n=== Respondida al Examen: " + exam.getTitle() + " ===");
        for (OpenEndedQuestion question : exam.getExamQuestions()) {
            System.out.println("Pregunta: " + question.getQuestionText());
            System.out.print("Tu respuesta: ");
            String answer = scanner.nextLine();
            response.addAnswer(question.getQuestionText(), answer);
        }

        // Añadir la respuesta al progreso
        progress.addExamResponse(exam, response);
        exam.addExamResponse(response);

        // Marcar el examen como entregado
        progress.updateActivityStatus(exam, ActivityStatus.SUBMITTED);

        // Guardar datos
        saveData();
        System.out.println("Examen entregado. Esperando revisión del profesor.");
    }

    /**
     * Permite al profesor realizar acciones relacionadas con las actividades.
     *
     * @param progress El progreso del Learning Path.
     * @param activity La actividad a realizar.
     */
    private void performActivity(Progress progress, Activity activity) {
        System.out.println("\n=== Realizando Actividad: " + activity.getTitle() + " ===");
        System.out.println("Descripción: " + activity.getDescription());
        switch (activity.getType()) {
            case "ResourceReview":
                ResourceReview rr = (ResourceReview) activity;
                System.out.println("Enlace al recurso: " + rr.getResourceLink());
                System.out.print("Presione Enter una vez haya revisado el recurso...");
                scanner.nextLine();
                progress.updateActivityStatus(activity, ActivityStatus.COMPLETED);
                // Guardar datos después de completar una actividad
                saveData();
                System.out.println("Actividad marcada como completada.");
                break;
            case "Assignment":
                Assignment assignment = (Assignment) activity;
                System.out.println("Instrucciones de entrega: " + assignment.getSubmissionInstructions());
                System.out.print("Escriba su respuesta o presione Enter para simular la entrega: ");
                scanner.nextLine();
                progress.updateActivityStatus(activity, ActivityStatus.SUBMITTED);
                // Guardar datos después de entregar una tarea
                saveData();
                System.out.println("Tarea entregada. Esperando revisión del profesor.");
                break;
            case "Quiz":
                Quiz quiz = (Quiz) activity;
                int correctAnswers = 0;
                List<Question> questions = quiz.getQuestions();
                for (Question q : questions) {
                    System.out.println("\nPregunta: " + q.getQuestionText());
                    String[] options = q.getOptions();
                    for (int i = 0; i < options.length; i++) {
                        System.out.println((i + 1) + ". " + options[i]);
                    }
                    int answer = readIntegerInput("Seleccione una opción: ", 1, options.length) - 1;
                    if (answer == q.getCorrectOptionIndex()) {
                        correctAnswers++;
                        System.out.println("Correcto!");
                    } else {
                        System.out.println("Incorrecto. " + q.getExplanation());
                    }
                }
                double score = (double) correctAnswers / questions.size() * 100;
                System.out.println("\n=== Resultado del Quiz ===");
                System.out.println("Su puntuación: " + String.format("%.2f", score) + "%");
                if (score >= quiz.getPassingScore()) {
                    progress.updateActivityStatus(activity, ActivityStatus.COMPLETED);
                    System.out.println("Ha aprobado el quiz.");
                } else {
                    progress.updateActivityStatus(activity, ActivityStatus.FAILED);
                    System.out.println("No ha alcanzado la puntuación mínima para aprobar.");
                }
                // Guardar datos después de completar un quiz
                saveData();
                break;
            default:
                System.out.println("Tipo de actividad desconocido.");
        }
    }

    /**
     * Método para leer una entrada entera del usuario dentro de un rango específico.
     *
     * @param prompt Mensaje a mostrar al usuario.
     * @param min    Valor mínimo permitido.
     * @param max    Valor máximo permitido.
     * @return El número entero ingresado por el usuario.
     */
    private int readIntegerInput(String prompt, int min, int max) {
        int result;
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                result = Integer.parseInt(input);
                if (result >= min && result <= max) {
                    break;
                } else {
                    System.out.println("Por favor, ingrese un número entre " + min + " y " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
            }
        }
        return result;
    }

    /**
     * Método para leer una entrada doble del usuario dentro de un rango específico.
     *
     * @param prompt Mensaje a mostrar al usuario.
     * @param min    Valor mínimo permitido.
     * @param max    Valor máximo permitido.
     * @return El número doble ingresado por el usuario.
     */
    private double readDoubleInput(String prompt, double min, double max) {
        double result;
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                result = Double.parseDouble(input);
                if (result >= min && result <= max) {
                    break;
                } else {
                    System.out.println("Por favor, ingrese un número entre " + min + " y " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Entrada no válida. Por favor, ingrese un número.");
            }
        }
        return result;
    }

    /**
     * Método para leer una entrada booleana del usuario.
     *
     * @param prompt Mensaje a mostrar al usuario.
     * @return True si el usuario ingresa 's' o 'S', false de lo contrario.
     */
    private boolean readBooleanInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("s")) {
                return true;
            } else if (input.equalsIgnoreCase("n")) {
                return false;
            } else {
                System.out.println("Entrada no válida. Por favor, ingrese 's' para sí o 'n' para no.");
            }
        }
    }
}


