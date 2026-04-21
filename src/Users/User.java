package Users;

public abstract class User {
    protected int id;
    protected String username;

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }

    @Override
    public String toString() {
        return "User: " + username + " (ID: " + id + ")";
    }
}
