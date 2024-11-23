package src.learningpath.users;

public class Teacher extends User {
    public Teacher(String username, String password, String name) {
        super(username, password, name);
    }

    @Override
    public String getRole() {
        return "Teacher";
    }
}
