package src.com.learningpath.main;

import src.com.learningpath.LearningPath;
import src.com.learningpath.Progress;
import src.com.learningpath.activities.*;
import src.com.learningpath.data.DataManager;
import src.com.learningpath.users.*;

import java.util.*;
import java.io.IOException;

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
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            saveData();
        }));
    }

    /**
     * Método principal que inicia la interfaz de consola.
     */
    public void start() {
        System.out.println("=== Sistema de Recomendación de Learning Paths ===");
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
            System.out.println("2. Ver Learning Paths");
            System.out.println("3. Ver Estudiantes Inscritos");
            System.out.println("4. Cerrar sesión");
            System.out.print("Seleccione una opción: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    createLearningPath(teacher);
                    break;
                case "2":
                    viewLearningPaths();
                    break;
                case "3":
                    viewEnrolledStudents(teacher);
                    break;
                case "4":
                    currentUser = null;
                    back = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    /**
     * Permite al profesor crear un nuevo Learning Path y agregar actividades.
     *
     * @param teacher El profesor que está creando el Learning Path.
     */
    private void createLearningPath(Teacher teacher) {
        System.out.print("Título: ");
        String title = scanner.nextLine();
        System.out.print("Descripción: ");
        String description = scanner.nextLine();
        System.out.print("Objetivos: ");
        String objectives = scanner.nextLine();
        int difficultyLevel = readIntegerInput("Nivel de dificultad (1-5): ", 1, 5);
        LearningPath lp = new LearningPath(title, description, objectives, difficultyLevel, teacher);
        boolean addingActivities = true;
        while (addingActivities) {
            System.out.println("\nAgregar una actividad:");
            System.out.println("1. Revisión de recurso");
            System.out.println("2. Tarea");
            System.out.println("3. Quiz");
            System.out.println("4. Finalizar");
            System.out.print("Seleccione una opción: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    Activity resourceReview = createResourceReview();
                    lp.addActivity(resourceReview);
                    break;
                case "2":
                    Activity assignment = createAssignment();
                    lp.addActivity(assignment);
                    break;
                case "3":
                    Activity quiz = createQuiz();
                    lp.addActivity(quiz);
                    break;
                case "4":
                    addingActivities = false;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
        learningPaths.add(lp);
        // Guardar datos inmediatamente después de crear un Learning Path
        saveData();
        System.out.println("Learning Path creado exitosamente.");
    }

    /**
     * Crea una actividad de tipo Revisión de Recurso.
     *
     * @return La actividad creada.
     */
    private Activity createResourceReview() {
        System.out.print("Título de la actividad: ");
        String title = scanner.nextLine();
        System.out.print("Descripción: ");
        String description = scanner.nextLine();
        System.out.print("Objetivo: ");
        String objective = scanner.nextLine();
        int difficultyLevel = readIntegerInput("Nivel de dificultad (1-5): ", 1, 5);
        int expectedDuration = readIntegerInput("Duración esperada (minutos): ", 1, Integer.MAX_VALUE);
        System.out.print("¿Es obligatoria? (s/n): ");
        boolean isMandatory = scanner.nextLine().equalsIgnoreCase("s");
        System.out.print("Enlace al recurso: ");
        String resourceLink = scanner.nextLine();
        return new ResourceReview(title, description, objective, difficultyLevel, expectedDuration, isMandatory, resourceLink);
    }

    /**
     * Crea una actividad de tipo Tarea.
     *
     * @return La actividad creada.
     */
    private Activity createAssignment() {
        System.out.print("Título de la actividad: ");
        String title = scanner.nextLine();
        System.out.print("Descripción: ");
        String description = scanner.nextLine();
        System.out.print("Objetivo: ");
        String objective = scanner.nextLine();
        int difficultyLevel = readIntegerInput("Nivel de dificultad (1-5): ", 1, 5);
        int expectedDuration = readIntegerInput("Duración esperada (minutos): ", 1, Integer.MAX_VALUE);
        System.out.print("¿Es obligatoria? (s/n): ");
        boolean isMandatory = scanner.nextLine().equalsIgnoreCase("s");
        System.out.print("Instrucciones de entrega: ");
        String submissionInstructions = scanner.nextLine();
        return new Assignment(title, description, objective, difficultyLevel, expectedDuration, isMandatory, submissionInstructions);
    }

    /**
     * Crea una actividad de tipo Quiz.
     *
     * @return La actividad creada.
     */
    private Activity createQuiz() {
        System.out.print("Título de la actividad: ");
        String title = scanner.nextLine();
        System.out.print("Descripción: ");
        String description = scanner.nextLine();
        System.out.print("Objetivo: ");
        String objective = scanner.nextLine();
        int difficultyLevel = readIntegerInput("Nivel de dificultad (1-5): ", 1, 5);
        int expectedDuration = readIntegerInput("Duración esperada (minutos): ", 1, Integer.MAX_VALUE);
        System.out.print("¿Es obligatoria? (s/n): ");
        boolean isMandatory = scanner.nextLine().equalsIgnoreCase("s");
        double passingScore = readDoubleInput("Calificación mínima para aprobar (0-100): ", 0, 100);
        List<Question> questions = new ArrayList<>();
        boolean addingQuestions = true;
        while (addingQuestions) {
            System.out.print("Texto de la pregunta: ");
            String questionText = scanner.nextLine();
            String[] options = new String[4];
            for (int i = 0; i < 4; i++) {
                System.out.print("Opción " + (i + 1) + ": ");
                options[i] = scanner.nextLine();
            }
            int correctOptionIndex = readIntegerInput("Índice de la opción correcta (1-4): ", 1, 4) - 1;
            System.out.print("Explicación: ");
            String explanation = scanner.nextLine();
            questions.add(new Question(questionText, options, correctOptionIndex, explanation));
            System.out.print("¿Agregar otra pregunta? (s/n): ");
            addingQuestions = scanner.nextLine().equalsIgnoreCase("s");
        }
        return new Quiz(title, description, objective, difficultyLevel, expectedDuration, isMandatory, questions, passingScore);
    }

    /**
     * Muestra todos los Learning Paths disponibles en el sistema.
     */
    private void viewLearningPaths() {
        if (learningPaths.isEmpty()) {
            System.out.println("No hay Learning Paths disponibles.");
            return;
        }
        System.out.println("\n=== Learning Paths Disponibles ===");
        for (int i = 0; i < learningPaths.size(); i++) {
            LearningPath lp = learningPaths.get(i);
            System.out.println((i + 1) + ". " + lp.getTitle() + " (Creado por: " + lp.getCreator().getName() + ")");
            System.out.println("   Descripción: " + lp.getDescription());
            System.out.println("   Objetivos: " + lp.getObjectives());
            System.out.println("   Nivel de Dificultad: " + lp.getDifficultyLevel());
            System.out.println("   Duración Total: " + lp.getDuration() + " minutos");
            System.out.println("   Versión: " + lp.getVersion());
        }
    }

    /**
     * Permite al profesor ver los estudiantes inscritos en sus Learning Paths.
     *
     * @param teacher El profesor que está realizando el seguimiento.
     */
    private void viewEnrolledStudents(Teacher teacher) {
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
        System.out.println("\n=== Sus Learning Paths ===");
        for (int i = 0; i < teacherLPs.size(); i++) {
            System.out.println((i + 1) + ". " + teacherLPs.get(i).getTitle());
        }
        int lpChoice = readIntegerInput("Seleccione un Learning Path para ver estudiantes (0 para regresar): ", 0, teacherLPs.size()) - 1;
        if (lpChoice == -1) {
            return;
        }
        LearningPath selectedLP = teacherLPs.get(lpChoice);
        List<Progress> lpProgresses = new ArrayList<>();
        for (Progress p : progresses) {
            if (p.getLearningPath().equals(selectedLP)) {
                lpProgresses.add(p);
            }
        }
        if (lpProgresses.isEmpty()) {
            System.out.println("Ningún estudiante está inscrito en este Learning Path.");
            return;
        }
        System.out.println("\n=== Estudiantes Inscritos en " + selectedLP.getTitle() + " ===");
        for (int i = 0; i < lpProgresses.size(); i++) {
            Progress p = lpProgresses.get(i);
            System.out.println((i + 1) + ". " + p.getStudent().getName() + " - " + String.format("%.2f", p.calculateCompletionPercentage()) + "% completado");
        }
        int studentChoice = readIntegerInput("Seleccione un estudiante para ver detalles (0 para regresar): ", 0, lpProgresses.size()) - 1;
        if (studentChoice == -1) {
            return;
        }
        Progress selectedProgress = lpProgresses.get(studentChoice);
        reviewStudentProgress(selectedProgress);
    }

    /**
     * Permite al profesor revisar y calificar el progreso de un estudiante.
     *
     * @param progress El progreso del estudiante seleccionado.
     */
    private void reviewStudentProgress(Progress progress) {
        System.out.println("\n=== Actividades del Estudiante: " + progress.getStudent().getName() + " ===");
        List<Activity> activities = progress.getLearningPath().getActivities();
        for (int i = 0; i < activities.size(); i++) {
            Activity activity = activities.get(i);
            ActivityStatus status = progress.getActivityStatus(activity);
            System.out.println((i + 1) + ". " + activity.getTitle() + " - " + status);
        }
        int activityChoice = readIntegerInput("Seleccione una actividad para revisar (0 para regresar): ", 0, activities.size()) - 1;
        if (activityChoice == -1) {
            return;
        }
        Activity selectedActivity = activities.get(activityChoice);
        ActivityStatus status = progress.getActivityStatus(selectedActivity);
        if (status == ActivityStatus.SUBMITTED) {
            System.out.println("\n=== Revisando Actividad: " + selectedActivity.getTitle() + " ===");
            // Aquí podrías agregar lógica para revisar la actividad y proporcionar retroalimentación
            System.out.print("Ingrese calificación para la actividad (0-100): ");
            double grade = readDoubleInput("Calificación: ", 0, 100);
            System.out.print("Ingrese comentarios para el estudiante: ");
            String feedback = scanner.nextLine();
            // Simulamos que el profesor califica y completa la actividad
            progress.updateActivityStatus(selectedActivity, ActivityStatus.COMPLETED);
            // Guardar datos después de calificar una actividad
            saveData();
            System.out.println("Actividad calificada y comentarios enviados al estudiante.");
            // Aquí podrías almacenar la calificación y feedback si es necesario
        } else {
            System.out.println("La actividad seleccionada no está en estado 'SUBMITTED' para revisión.");
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
        int choice = readIntegerInput("Seleccione un Learning Path para inscribirse (0 para regresar): ", 0, availableLPs.size()) - 1;
        if (choice == -1) {
            return;
        }
        LearningPath selectedLP = availableLPs.get(choice);
        Progress progress = new Progress(student, selectedLP);
        progresses.add(progress);
        // Guardar datos después de inscribirse en un Learning Path
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
        int choice = readIntegerInput("Seleccione un Learning Path para ver detalles (0 para regresar): ", 0, myProgresses.size()) - 1;
        if (choice == -1) {
            return;
        }
        Progress selectedProgress = myProgresses.get(choice);
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
            System.out.println((i + 1) + ". " + activity.getTitle() + " - " + status);
        }
        int choice = readIntegerInput("Seleccione una actividad para realizar (0 para regresar): ", 0, activities.size()) - 1;
        if (choice == -1) {
            return;
        }
        Activity selectedActivity = activities.get(choice);
        ActivityStatus status = progress.getActivityStatus(selectedActivity);
        if (status == ActivityStatus.COMPLETED) {
            System.out.println("Esta actividad ya ha sido completada.");
            return;
        }
        performActivity(progress, selectedActivity);
    }

    /**
     * Permite al estudiante realizar una actividad específica.
     *
     * @param progress  El progreso del Learning Path.
     * @param activity  La actividad a realizar.
     */
    private void performActivity(Progress progress, Activity activity) {
        System.out.println("\n=== Realizando Actividad: " + activity.getTitle() + " ===");
        System.out.println("Descripción: " + activity.getDescription());
        switch (activity.getType()) {
            case "Resource Review":
                System.out.println("Enlace al recurso: " + ((ResourceReview) activity).getResourceLink());
                System.out.print("Presione Enter una vez haya revisado el recurso...");
                scanner.nextLine();
                progress.updateActivityStatus(activity, ActivityStatus.COMPLETED);
                // Guardar datos después de completar una actividad
                saveData();
                System.out.println("Actividad marcada como completada.");
                break;
            case "Assignment":
                System.out.println("Instrucciones de entrega: " + ((Assignment) activity).getSubmissionInstructions());
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
     * Lee una entrada entera del usuario dentro de un rango específico.
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
     * Lee una entrada doble del usuario dentro de un rango específico.
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
}
