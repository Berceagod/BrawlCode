package matches;
import users.RegularUser;

public class Submission {
    private int submissionId;
    private RegularUser user;
    private String codeSnippet;
    private boolean isCorrect;

    public Submission(int submissionId, RegularUser user, String codeSnippet, boolean isCorrect) {
        this.submissionId = submissionId;
        this.user = user;
        this.codeSnippet = codeSnippet;
        this.isCorrect = isCorrect;
    }

    public boolean isCorrect() { return isCorrect; }
    public RegularUser getUser() { return user; }
}
