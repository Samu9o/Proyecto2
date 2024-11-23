package src.com.learningpath.main;

import src.com.learningpath.LearningPath;
import src.com.learningpath.Progress;
import src.com.learningpath.activities.*;
import src.com.learningpath.data.DataManager;
import src.com.learningpath.users.*;

import java.util.*;

public class ConsoleInterface {
    private Scanner scanner;
    private List<User> users;
    private List<LearningPath> learningPaths;
    private List<Progress> progresses;
    private User currentUser;

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
        }
    }

    public void start() {
        System.out.println("=== Learning Path Recommendation System ===");
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
        // Guardar datos antes de salir
        try {
            DataManager.saveUsers(users);
            DataManager.saveLearningPaths(learningPaths);
            DataManager.saveProgresses(progresses);
        } catch (Exception e) {
            System.out.println("Error al guardar datos: " + e.getMessage());
        }
        System.out.println("Hasta luego.");
    }

    private void login() {
        System.out.print("Nombre de usuario: ");
        String username = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();
        Optional<User> userOpt = users.stream().filter(u -> u.getUsername().equals(username)).findFirst();
        if (userOpt.isPresent() && userOpt.get().authenticate(password)) {
            currentUser = userOpt.get();
            System.out.println("Bienvenido, " + currentUser.getName() + " (" + currentUser.getRole() + ")");
        } else {
            System.out.println("Credenciales incorrectas.");
        }
    }

    private void register() {
        System.out.print("Nombre de usuario: ");
        String username = scanner.nextLine();
        System.out.print("Contraseña: ");
        String password = scanner.nextLine();
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
        System.out.println("Usuario registrado exitosamente.");
    }

    private void teacherMenu() {
        Teacher teacher = (Teacher) currentUser;
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Menú de Profesor ===");
            System.out.println("1. Crear Learning Path");
            System.out.println("2. Ver Learning Paths");
            System.out.println("3. Cerrar sesión");
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
                    currentUser = null;
                    back = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private void createLearningPath(Teacher teacher) {
        System.out.print("Título: ");
        String title = scanner.nextLine();
        System.out.print("Descripción: ");
        String description = scanner.nextLine();
        System.out.print("Objetivos: ");
        String objectives = scanner.nextLine();
        System.out.print("Nivel de dificultad (1-5): ");
        int difficultyLevel = Integer.parseInt(scanner.nextLine());
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
        System.out.println("Learning Path creado exitosamente.");
    }

    private Activity createResourceReview() {
        System.out.print("Título de la actividad: ");
        String title = scanner.nextLine();
        System.out.print("Descripción: ");
        String description = scanner.nextLine();
        System.out.print("Objetivo: ");
        String objective = scanner.nextLine();
        System.out.print("Nivel de dificultad (1-5): ");
        int difficultyLevel = Integer.parseInt(scanner.nextLine());
        System.out.print("Duración esperada (minutos): ");
        int expectedDuration = Integer.parseInt(scanner.nextLine());
        System.out.print("¿Es obligatoria? (s/n): ");
        boolean isMandatory = scanner.nextLine().equalsIgnoreCase("s");
        System.out.print("Enlace al recurso: ");
        String resourceLink = scanner.nextLine();
        return new ResourceReview(title, description, objective, difficultyLevel, expectedDuration, isMandatory, resourceLink);
    }

    private Activity createAssignment() {
        System.out.print("Título de la actividad: ");
        String title = scanner.nextLine();
        System.out.print("Descripción: ");
        String description = scanner.nextLine();
        System.out.print("Objetivo: ");
        String objective = scanner.nextLine();
        System.out.print("Nivel de dificultad (1-5): ");
        int difficultyLevel = Integer.parseInt(scanner.nextLine());
        System.out.print("Duración esperada (minutos): ");
        int expectedDuration = Integer.parseInt(scanner.nextLine());
        System.out.print("¿Es obligatoria? (s/n): ");
        boolean isMandatory = scanner.nextLine().equalsIgnoreCase("s");
        System.out.print("Instrucciones de entrega: ");
        String submissionInstructions = scanner.nextLine();
        return new Assignment(title, description, objective, difficultyLevel, expectedDuration, isMandatory, submissionInstructions);
    }

    private Activity createQuiz() {
        System.out.print("Título de la actividad: ");
        String title = scanner.nextLine();
        System.out.print("Descripción: ");
        String description = scanner.nextLine();
        System.out.print("Objetivo: ");
        String objective = scanner.nextLine();
        System.out.print("Nivel de dificultad (1-5): ");
        int difficultyLevel = Integer.parseInt(scanner.nextLine());
        System.out.print("Duración esperada (minutos): ");
        int expectedDuration = Integer.parseInt(scanner.nextLine());
        System.out.print("¿Es obligatoria? (s/n): ");
        boolean isMandatory = scanner.nextLine().equalsIgnoreCase("s");
        System.out.print("Calificación mínima para aprobar: ");
        double passingScore = Double.parseDouble(scanner.nextLine());
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
            System.out.print("Índice de la opción correcta (1-4): ");
            int correctOptionIndex = Integer.parseInt(scanner.nextLine()) - 1;
            System.out.print("Explicación: ");
            String explanation = scanner.nextLine();
            questions.add(new Question(questionText, options, correctOptionIndex, explanation));
            System.out.print("¿Agregar otra pregunta? (s/n): ");
            addingQuestions = scanner.nextLine().equalsIgnoreCase("s");
        }
        return new Quiz(title, description, objective, difficultyLevel, expectedDuration, isMandatory, questions, passingScore);
    }

    private void viewLearningPaths() {
        if (learningPaths.isEmpty()) {
            System.out.println("No hay Learning Paths disponibles.");
            return;
        }
        for (int i = 0; i < learningPaths.size(); i++) {
            LearningPath lp = learningPaths.get(i);
            System.out.println((i + 1) + ". " + lp.getTitle() + " (Creado por: " + lp.getCreator().getName() + ")");
        }
    }

    private void studentMenu() {
        Student student = (Student) currentUser;
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Menú de Estudiante ===");
            System.out.println("1. Ver Learning Paths disponibles");
            System.out.println("2. Cerrar sesión");
            System.out.print("Seleccione una opción: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    enrollInLearningPath(student);
                    break;
                case "2":
                    currentUser = null;
                    back = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    private void enrollInLearningPath(Student student) {
        if (learningPaths.isEmpty()) {
            System.out.println("No hay Learning Paths disponibles.");
            return;
        }
        for (int i = 0; i < learningPaths.size(); i++) {
            LearningPath lp = learningPaths.get(i);
            System.out.println((i + 1) + ". " + lp.getTitle() + " (Creado por: " + lp.getCreator().getName() + ")");
        }
        System.out.print("Seleccione un Learning Path para inscribirse: ");
        int choice = Integer.parseInt(scanner.nextLine()) - 1;
        if (choice >= 0 && choice < learningPaths.size()) {
            LearningPath selectedLP = learningPaths.get(choice);
            Progress progress = new Progress(student, selectedLP);
            progresses.add(progress);
            System.out.println("Inscrito en " + selectedLP.getTitle());
        } else {
            System.out.println("Selección no válida.");
        }
    }
}
