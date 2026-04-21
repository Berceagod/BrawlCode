package Users;

public class RegularUser extends User implements Comparable<RegularUser> {
    protected int eloRating;

    public RegularUser(int id, String username) {
        super(id, username);
        this.eloRating = 1000;
    }

    public int getEloRating() { return eloRating; }
    public void setEloRating(int eloRating) { this.eloRating = eloRating; }

    @Override
    public int compareTo(RegularUser other) {
        return Integer.compare(other.eloRating, this.eloRating);
    }

    @Override
    public String toString() {
        return username + " (Rating: " + eloRating + ")";
    }
}
