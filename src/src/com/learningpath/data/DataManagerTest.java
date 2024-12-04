package src.com.learningpath.data;

import src.com.learningpath.LearningPath;
import src.com.learningpath.users.Teacher;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataManagerTest {

    @Test
    public void testSaveAndLoadLearningPaths() throws Exception {
        // Creamos un directorio temporal para pruebas
        String tempDataFolder = "temp_data/";
        new File(tempDataFolder).mkdirs();

        // Establecemos la carpeta de datos temporal
        DataManager.setDataFolder(tempDataFolder);

        // Creamos una lista de LearningPaths
        Teacher teacher = new Teacher("jdoe", "password123", "John Doe");
        LearningPath lp1 = new LearningPath("LP1", "Description 1", "Objectives 1", 1, teacher);
        LearningPath lp2 = new LearningPath("LP2", "Description 2", "Objectives 2", 2, teacher);
        List<LearningPath> learningPathsToSave = new ArrayList<>();
        learningPathsToSave.add(lp1);
        learningPathsToSave.add(lp2);

        // Guardamos los LearningPaths
        DataManager.saveLearningPaths(learningPathsToSave);

        // Cargamos los LearningPaths
        List<LearningPath> loadedLearningPaths = DataManager.loadLearningPaths();

        // Verificamos que los datos cargados sean correctos
        assertEquals(2, loadedLearningPaths.size());
        assertEquals("LP1", loadedLearningPaths.get(0).getTitle());
        assertEquals("LP2", loadedLearningPaths.get(1).getTitle());

        // Limpiamos los archivos temporales
        new File(tempDataFolder + "learning_paths.dat").delete();
        new File(tempDataFolder).delete();
    }
}
