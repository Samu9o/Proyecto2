package src.learningpath.users;

public class Student extends User {
    public Student(String username, String password, String name) {
        super(username, password, name);
    }

    @Override
    public String getRole() {
        return "Student";
    }
}

