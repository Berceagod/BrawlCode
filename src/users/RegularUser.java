package users;

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
        int eloCompare = Integer.compare(other.eloRating, this.eloRating);
        if (eloCompare == 0) {
            return Integer.compare(this.id, other.id);
        }
        return eloCompare;
    }

    @Override
    public String toString() {
        return username + " (Rating: " + eloRating + ")";
    }
}
