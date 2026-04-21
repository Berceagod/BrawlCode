package Users;

public class Admin extends User {
    private String accessLevel;

    public Admin(int id, String username, String accessLevel) {
        super(id, username);
        this.accessLevel = accessLevel;
    }

    public String getAccessLevel() { return accessLevel; }

    @Override
    public String toString() {
        return "[ADMIN] " + username + " (Nivel: " + accessLevel + ")";
    }
}
