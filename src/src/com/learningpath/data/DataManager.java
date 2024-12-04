package src.com.learningpath.data;

import src.com.learningpath.LearningPath;
import src.com.learningpath.Progress;
import src.com.learningpath.users.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
	
	private static String DATA_FOLDER = "data/";
	
   

    public static void saveUsers(List<User> users) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FOLDER + "users.dat"))) {
            oos.writeObject(users);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<User> loadUsers() throws IOException, ClassNotFoundException {
        File file = new File(DATA_FOLDER + "users.dat");
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<User>) ois.readObject();
        }
    }

    public static void saveLearningPaths(List<LearningPath> learningPaths) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FOLDER + "learning_paths.dat"))) {
            oos.writeObject(learningPaths);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<LearningPath> loadLearningPaths() throws IOException, ClassNotFoundException {
        File file = new File(DATA_FOLDER + "learning_paths.dat");
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<LearningPath>) ois.readObject();
        }
    }

    public static void saveProgresses(List<Progress> progresses) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FOLDER + "progresses.dat"))) {
            oos.writeObject(progresses);
        }
    }
    public static void setDataFolder(String folder) {
        DATA_FOLDER = folder;
    }

    @SuppressWarnings("unchecked")
    public static List<Progress> loadProgresses() throws IOException, ClassNotFoundException {
        File file = new File(DATA_FOLDER + "progresses.dat");
        if (!file.exists()) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<Progress>) ois.readObject();
        }
    }

	
	}

