package matches;
import users.RegularUser;
import problems.Problem;

public class Match {
    private int matchId;
    private RegularUser player1;
    private RegularUser player2;
    private Problem problem;
    private boolean isFinished;

    public Match(int matchId, RegularUser player1, RegularUser player2, Problem problem) {
        this.matchId = matchId;
        this.player1 = player1;
        this.player2 = player2;
        this.problem = problem;
        this.isFinished = false;
    }

    public int getMatchId() { return matchId; }
    public RegularUser getPlayer1() { return player1; }
    public RegularUser getPlayer2() { return player2; }
    public Problem getProblem() { return problem; }
    public boolean isFinished() { return isFinished; }
    public void setFinished(boolean finished) { isFinished = finished; }

    @Override
    public String toString() {
        return "Match #" + matchId + ": " + player1.getUsername() + " vs " + player2.getUsername() + " | Problema: " + problem.getTitle();
    }
}
