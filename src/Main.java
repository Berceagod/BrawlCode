import services.PlatformService;
import users.Admin;
import users.PremiumUser;
import users.RegularUser;
import problems.AlgorithmicProblem;
import problems.DatabaseProblem;
import matches.Match;

public class Main {
    public static void main(String[] args) {
        PlatformService service = new PlatformService();


        System.out.println("Action 1: Add Users");
        Admin admin = new Admin(99, "SystemAdmin", 3);
        RegularUser player1 = new RegularUser(1, "DevNinja");
        PremiumUser player2 = new PremiumUser(2, "CodeMasterPro", "Gold");
        RegularUser player3 = new RegularUser(3, "NoobCoder");

        service.addUser(admin);
        service.addUser(player1);
        service.addUser(player2);
        service.addUser(player3);
        System.out.println("Users successfully added.\n");

        System.out.println("Action 2: Add Problems");
        AlgorithmicProblem p1 = new AlgorithmicProblem(101, "Two Sum", "Easy", 2.0);
        DatabaseProblem p2 = new DatabaseProblem(102, "Top Earners", "Medium", "EmployeeDB");
        AlgorithmicProblem p3 = new AlgorithmicProblem(103, "Dijkstra Paths", "Hard", 5.0);

        service.addProblem(p1);
        service.addProblem(p2);
        service.addProblem(p3);
        System.out.println("Problems successfully added.\n");

        System.out.println("Action 3: Display Leaderboard");
        service.displayLeaderboard();

        System.out.println("\nAction 4: Create Matches");
        Match match1 = service.createMatch(1001, player1, player2, p1);
        Match match2 = service.createMatch(1002, player2, player3, p3);

        System.out.println("\nAction 5: Filter Problems by Difficulty");
        service.getProblemsByDifficulty("Easy");

        System.out.println("\nAction 9: Find Problem by ID (102)");
        service.displayProblem(102);

        System.out.println("\nAction 10: Display Active Matches");
        service.displayActiveMatches();

        System.out.println("\nAction 6 & 7: Submit Code and Finish Match");
        service.submitCode(1, match1, player1, "return a - b;", false);
        service.submitCode(2, match1, player2, "return new int[]{i, j};", true);

        System.out.println("\nAction 3 (again): Updated Leaderboard");
        service.displayLeaderboard();

        System.out.println("\nAction 8: Match History for CodeMasterPro");
        service.getUserHistory(player2);


    }
}