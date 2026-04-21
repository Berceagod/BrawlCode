package Users;

public class PremiumUser extends RegularUser {
    private String premiumBadge;

    public PremiumUser(int id, String username, String premiumBadge) {
        super(id, username);
        this.premiumBadge = premiumBadge;
    }

    public String getPremiumBadge() { return premiumBadge; }

    @Override
    public String toString() {
        return "[" + premiumBadge + "] " + super.toString();
    }
}