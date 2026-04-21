package users;

public class Admin extends User {
    private int accessLevel;

    public Admin(int id, String username, int accessLevel) {
        super(id, username);
        this.accessLevel = accessLevel;
    }

    public int getAccessLevel() { return accessLevel; }

    @Override
    public String toString() {
        return "[ADMIN] " + username + " (Access Level: " + accessLevel + ")";
    }
}
