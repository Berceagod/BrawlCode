package services;

import matches.Match;
import matches.Submission;
import problems.Problem;
import users.RegularUser;
import users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PlatformService {

    private List<User> allUsers = new ArrayList<>();
    private List<Problem> problemsList = new ArrayList<>();
    private List<Match> matchesList = new ArrayList<>();
    private List<Submission> submissionsList = new ArrayList<>();


    private Set<RegularUser> leaderboard = new TreeSet<>();


    public void addUser(User user) {
        allUsers.add(user);

        if (user instanceof RegularUser) {
            leaderboard.add((RegularUser) user);
        }
    }

    public void addProblem(Problem problem) {
        problemsList.add(problem);
    }


    public void displayLeaderboard() {
        System.out.println("\n=== LEADERBOARD ===");
        for (RegularUser u : leaderboard) {
            System.out.println(u);
        }
    }

    public Match createMatch(int matchId, RegularUser p1, RegularUser p2, Problem problem) {
        Match match = new Match(matchId, p1, p2, problem);
        matchesList.add(match);
        System.out.println("Match Created: " + match.toString());
        return match;
    }


    public void getProblemsByDifficulty(String difficulty) {
        System.out.println("\n=== Problems of difficulty : " + difficulty + " ===");
        for (Problem p : problemsList) {
            if (p.getDifficulty().equalsIgnoreCase(difficulty)) {
                System.out.println(p.getProblemDetails());
            }
        }
    }


    public void submitCode(int subId, Match match, RegularUser user, String code, boolean isCorrect) {
        Submission sub = new Submission(subId, user, code, isCorrect);
        submissionsList.add(sub);
        System.out.println(user.getUsername() + "submitted code . Correct : " + isCorrect);

        if(isCorrect && !match.isFinished()) {
            finishMatch(match, user);
        }
    }


    public void finishMatch(Match match, RegularUser winner) {
        match.setFinished(true);


        RegularUser loser;
        if (match.getPlayer1().equals(winner)) {
            loser = match.getPlayer2();
        } else {
            loser = match.getPlayer1();
        }


        leaderboard.remove(winner);
        leaderboard.remove(loser);

        winner.setEloRating(winner.getEloRating() + 25);
        loser.setEloRating(loser.getEloRating() - 15);

        leaderboard.add(winner);
        leaderboard.add(loser);

        System.out.println("Match finished. Winner : " + winner.getUsername());
    }


    public void getUserHistory(RegularUser user) {
        System.out.println("\n=== History of  " + user.getUsername() + " ===");
        for(Match m : matchesList) {
            if(m.getPlayer1().equals(user) || m.getPlayer2().equals(user)) {
                System.out.println(m);
            }
        }
    }


    public void displayProblem(int id) {
        for(Problem p : problemsList) {
            if(p.getId() == id) {
                System.out.println(p.getProblemDetails());
                return;
            }
        }
        System.out.println("Problem not found.");
    }


    public void displayActiveMatches() {
        System.out.println("\n=== Active Matches ===");
        for(Match m : matchesList) {
            if(!m.isFinished()) {
                System.out.println(m);
            }
        }
    }
}