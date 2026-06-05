package services;

import matches.Match;
import matches.Submission;
import problems.Problem;
import users.RegularUser;
import users.User;
import repositories.*;

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

    public PlatformService() {
        loadData();
    }

    public void loadData() {
        allUsers.clear();
        problemsList.clear();
        matchesList.clear();
        submissionsList.clear();
        leaderboard.clear();

        allUsers.addAll(UserRepository.getInstance().readAll());
        for(User u : allUsers) {
            if(u instanceof RegularUser) {
                leaderboard.add((RegularUser) u);
            }
        }
        problemsList.addAll(ProblemRepository.getInstance().readAll());
        matchesList.addAll(MatchRepository.getInstance().readAll());
        submissionsList.addAll(SubmissionRepository.getInstance().readAll());
    }

    public User findUserById(int id) {
        for (User u : allUsers) {
            if (u.getId() == id) return u;
        }
        return UserRepository.getInstance().read(id);
    }

    public Problem findProblemById(int id) {
        for (Problem p : problemsList) {
            if (p.getId() == id) return p;
        }
        return ProblemRepository.getInstance().read(id);
    }

    public Match findMatchById(int id) {
        for (Match m : matchesList) {
            if (m.getMatchId() == id) return m;
        }
        return MatchRepository.getInstance().read(id);
    }

    public boolean addUser(User user) {
        AuditService.getInstance().logAction("addUser");
        if (findUserById(user.getId()) == null) {
            allUsers.add(user);
            UserRepository.getInstance().create(user);
            if (user instanceof RegularUser) {
                leaderboard.add((RegularUser) user);
            }
            return true;
        }
        return false;
    }

    public boolean addProblem(Problem problem) {
        AuditService.getInstance().logAction("addProblem");
        if (findProblemById(problem.getId()) == null) {
            problemsList.add(problem);
            ProblemRepository.getInstance().create(problem);
            return true;
        }
        return false;
    }

    public boolean deleteUser(int id) {
        AuditService.getInstance().logAction("deleteUser");
        User user = findUserById(id);
        if (user != null) {
            allUsers.remove(user);
            if (user instanceof RegularUser) {
                leaderboard.remove((RegularUser) user);
            }
            UserRepository.getInstance().delete(id);
            return true;
        }
        return false;
    }

    public boolean deleteProblem(int id) {
        AuditService.getInstance().logAction("deleteProblem");
        Problem problem = findProblemById(id);
        if (problem != null) {
            problemsList.remove(problem);
            ProblemRepository.getInstance().delete(id);
            return true;
        }
        return false;
    }

    public void displayLeaderboard() {
        AuditService.getInstance().logAction("displayLeaderboard");
        System.out.println("\n=== LEADERBOARD (Top Players) ===");
        System.out.printf("%-5s | %-20s | %-5s%n", "ID", "Username", "ELO");
        System.out.println("----------------------------------------");
        for (RegularUser u : leaderboard) {
            System.out.printf("%-5d | %-20s | %-5d%n", u.getId(), u.getUsername(), u.getEloRating());
        }
    }

    public Match createMatch(int matchId, RegularUser p1, RegularUser p2, Problem problem) {
        AuditService.getInstance().logAction("createMatch");
        for (Match m : matchesList) {
            if (m.getMatchId() == matchId) {
                return null;
            }
        }
        Match match = new Match(matchId, p1, p2, problem);
        matchesList.add(match);
        MatchRepository.getInstance().create(match);
        System.out.println("Match Created: " + match.toString());
        return match;
    }

    public void getProblemsByDifficulty(String difficulty) {
        AuditService.getInstance().logAction("getProblemsByDifficulty");
        System.out.println("\n=== Problems of difficulty : " + difficulty + " ===");
        for (Problem p : problemsList) {
            if (p.getDifficulty().equalsIgnoreCase(difficulty)) {
                System.out.println("[ID: " + p.getId() + "] " + p.getProblemDetails());
            }
        }
    }

    public void submitCode(int subId, Match match, RegularUser user, String code, boolean isCorrect) {
        AuditService.getInstance().logAction("submitCode");
        for (Submission s : submissionsList) {
            if (s.getSubmissionId() == subId) {
                return;
            }
        }
        Submission sub = new Submission(subId, user, code, isCorrect);
        submissionsList.add(sub);
        SubmissionRepository.getInstance().create(sub);
        System.out.println(user.getUsername() + " submitted code . Correct : " + isCorrect);

        if(isCorrect && !match.isFinished()) {
            finishMatch(match, user);
        }
    }

    public void finishMatch(Match match, RegularUser winner) {
        AuditService.getInstance().logAction("finishMatch");
        match.setFinished(true);
        MatchRepository.getInstance().update(match);

        RegularUser loser;
        if (match.getPlayer1().getId() == winner.getId()) {
            loser = match.getPlayer2();
        } else {
            loser = match.getPlayer1();
        }

        leaderboard.remove(winner);
        leaderboard.remove(loser);

        winner.setEloRating(winner.getEloRating() + 25);
        loser.setEloRating(loser.getEloRating() - 15);

        UserRepository.getInstance().update(winner);
        UserRepository.getInstance().update(loser);

        leaderboard.add(winner);
        leaderboard.add(loser);

        System.out.println("Match finished. Winner : " + winner.getUsername());
    }

    public void getUserHistory(RegularUser user) {
        AuditService.getInstance().logAction("getUserHistory");
        System.out.println("\n=== Match History for " + user.getUsername() + " (ID: " + user.getId() + ") ===");
        for(Match m : matchesList) {
            if(m.getPlayer1().getId() == user.getId() || m.getPlayer2().getId() == user.getId()) {
                System.out.println("[Match ID: " + m.getMatchId() + "] " + m);
            }
        }
    }

    public void displayAllUsers() {
        AuditService.getInstance().logAction("displayAllUsers");
        System.out.println("\n=== ALL REGISTERED USERS ===");
        System.out.printf("%-5s | %-20s | %-10s | %-5s%n", "ID", "Username", "Type", "ELO");
        System.out.println("------------------------------------------------------------");
        for (User u : allUsers) {
            String type = u.getClass().getSimpleName();
            int elo = (u instanceof RegularUser) ? ((RegularUser) u).getEloRating() : 0;
            System.out.printf("%-5d | %-20s | %-10s | %-5d%n", u.getId(), u.getUsername(), type, elo);
        }
    }

    public void displayDetailedProblems() {
        AuditService.getInstance().logAction("displayDetailedProblems");
        System.out.println("\n=== PROBLEM ARCHIVE (Detailed) ===");
        System.out.printf("%-5s | %-25s | %-10s | %-15s%n", "ID", "Title", "Diff", "Category");
        System.out.println("------------------------------------------------------------------");
        for (Problem p : problemsList) {
            String category = p.getClass().getSimpleName().replace("Problem", "");
            System.out.printf("%-5d | %-25s | %-10s | %-15s%n", p.getId(), p.getTitle(), p.getDifficulty(), category);
        }
    }

    public void displayPlatformStats() {
        AuditService.getInstance().logAction("displayPlatformStats");
        System.out.println("\n=== BRAWLCODE ANALYTICS ===");
        System.out.println("Total Users: " + allUsers.size());
        System.out.println("Total Problems: " + problemsList.size());
        System.out.println("Matches Played: " + matchesList.size());
        System.out.println("Total Submissions: " + submissionsList.size());
        
        long activeMatches = matchesList.stream().filter(m -> !m.isFinished()).count();
        System.out.println("Currently Active Matches: " + activeMatches);
    }

    public void displayProblem(int id) {
        AuditService.getInstance().logAction("displayProblem");
        if (id == -1) {
            System.out.println("\n--- ALL PROBLEMS ---");
            for (Problem p : problemsList) {
                System.out.println("[ID: " + p.getId() + "] " + p.getProblemDetails());
            }
            return;
        }
        for(Problem p : problemsList) {
            if(p.getId() == id) {
                System.out.println("[ID: " + p.getId() + "] " + p.getProblemDetails());
                return;
            }
        }
        System.out.println("Problem not found.");
    }

    public void displayActiveMatches() {
        AuditService.getInstance().logAction("displayActiveMatches");
        System.out.println("\n=-= Active Matches =-=");
        boolean found = false;
        for(Match m : matchesList) {
            if(!m.isFinished()) {
                System.out.println("[Match ID: " + m.getMatchId() + "] " + m);
                found = true;
            }
        }
        if (!found) System.out.println("No active matches at the moment.");
    }
}
