import services.PlatformService;
import users.Admin;
import users.PremiumUser;
import users.RegularUser;
import users.User;
import problems.Problem;
import problems.AlgorithmicProblem;
import problems.DatabaseProblem;
import matches.Match;

import java.util.Scanner;

/**
 * Enhanced Professional CLI for BrawlCode Platform.
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static PlatformService service;

    public static void main(String[] args) {
        try {
            config.DatabaseConfiguration.getInstance();
            service = new PlatformService();
        } catch (Exception e) {
            System.err.println("[System Error] Critical failure during startup: " + e.getMessage());
            return;
        }

        printBanner();

        boolean running = true;
        while (running) {
            printMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": showLeaderboard(); break;
                case "2": manageUsers(); break;
                case "3": manageProblems(); break;
                case "4": startMatchmaking(); break;
                case "5": showHistory(); break;
                case "6": showAnalytics(); break;
                case "0":
                    running = false;
                    System.out.println("\n[System] Shutting down BrawlCode... Goodbye!");
                    break;
                default:
                    System.out.println("\n[Error] Invalid option.");
                    pressEnterToContinue();
            }
        }
    }

    private static void printBanner() {
        System.out.println("\n" +
                "▄▄▄▄    ██▀███   ▄▄▄       █     █░ ██▓        ▄████▄   ▒█████  ▓█████▄ ▓█████\n" +
                "▓█████▄ ▓██ ▒ ██▒▒████▄    ▓█░ █ ░█░▓██▒       ▒██▀ ▀█  ▒██▒  ██▒▒██▀ ██▌▓█   ▀\n" +
                "▒██▒ ▄██▓██ ░▄█ ▒▒██  ▀█▄  ▒█░ █ ░█ ▒██░       ▒▓█    ▄ ▒██░  ██▒░██   █▌▒███\n" +
                "▒██░█▀  ▒██▀▀█▄  ░██▄▄▄▄██ ░█░ █ ░█ ▒██░       ▒▓▓▄ ▄██▒▒██   ██░░▓█▄   ▌▒▓█  ▄\n" +
                "░▓█  ▀█▓░██▓ ▒██▒ ▓█   ▓██▒░░██▒██▓ ░██████▒   ▒ ▓███▀ ░░ ████▓▒░░▒████▓ ░▒████▒\n" +
                "░▒▓███▀▒░ ▒▓ ░▒▓░ ▒▒   ▓▒█░░ ▓░▒ ▒  ░ ▒░▓  ░   ░ ░▒ ▒  ░░ ▒░▒░▒░  ▒▒▓  ▒ ░░ ▒░ ░\n" +
                "▒░▒   ░   ░▒ ░ ▒░  ▒   ▒▒ ░  ▒ ░ ░  ░ ░ ▒  ░     ░  ▒     ░ ▒ ▒░  ░ ▒  ▒  ░ ░  ░\n" +
                "░    ░   ░░   ░   ░   ▒     ░   ░    ░ ░      ░        ░ ░ ░ ▒   ░ ░  ░    ░\n" +
                "░         ░           ░  ░    ░        ░  ░   ░ ░          ░ ░     ░       ░  ░\n" +
                "░                                        ░                  ░ \n" +
                "                                                  THE ULTIMATE ARENA v2.1\n");
    }

    private static void printMainMenu() {
        System.out.println("\n--- DASHBOARD ---");
        System.out.println("1. Global Leaderboard (By ELO)");
        System.out.println("2. User Management (List/Add/Find)");
        System.out.println("3. Problem Archive (List/Details/Filter)");
        System.out.println("4. Battle Arena (Matchmaking)");
        System.out.println("5. Match History");
        System.out.println("6. Platform Analytics");
        System.out.println("0. Exit");
        System.out.print("Action > ");
    }

    private static void pressEnterToContinue() {
        System.out.println("\n[Press Enter to return]");
        scanner.nextLine();
    }

    private static void showLeaderboard() {
        service.displayLeaderboard();
        pressEnterToContinue();
    }

    private static void manageUsers() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- USER MANAGEMENT ---");
            System.out.println("1. List All Users (with IDs)");
            System.out.println("2. Add Regular User");
            System.out.println("3. Add Premium User");
            System.out.println("4. Add Admin");
            System.out.println("5. Find User by ID");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("0")) { back = true; continue; }

            try {
                switch (choice) {
                    case "1":
                        service.displayAllUsers();
                        pressEnterToContinue();
                        break;
                    case "2":
                    case "3":
                    case "4":
                        System.out.print("Enter ID: ");
                        int id = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Enter Username: ");
                        String name = scanner.nextLine().trim();

                        boolean success;
                        if (choice.equals("2")) {
                            success = service.addUser(new RegularUser(id, name));
                        } else if (choice.equals("3")) {
                            System.out.print("Enter Badge (e.g., Gold): ");
                            String badge = scanner.nextLine().trim();
                            success = service.addUser(new PremiumUser(id, name, badge));
                        } else {
                            System.out.print("Access Level (1-5): ");
                            int level = Integer.parseInt(scanner.nextLine().trim());
                            success = service.addUser(new Admin(id, name, level));
                        }
                        
                        System.out.println(success ? "[Success] User added." : "[Error] ID already exists.");
                        pressEnterToContinue();
                        break;
                    case "5":
                        System.out.print("User ID: ");
                        int findId = Integer.parseInt(scanner.nextLine().trim());
                        User u = service.findUserById(findId);
                        System.out.println(u != null ? "\n" + u : "[Error] Not found.");
                        pressEnterToContinue();
                        break;
                    default:
                        System.out.println("[Error] Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("[Error] Invalid input: " + e.getMessage());
                pressEnterToContinue();
            }
        }
    }

    private static void manageProblems() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- PROBLEM ARCHIVE ---");
            System.out.println("1. List All Problems (Quick View)");
            System.out.println("2. Detailed Problem List (IDs & Categories)");
            System.out.println("3. Filter by Difficulty");
            System.out.println("4. View Full Problem Description (by ID)");
            System.out.println("0. Back");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("0")) { back = true; continue; }

            switch (choice) {
                case "1":
                    service.displayProblem(-1);
                    pressEnterToContinue();
                    break;
                case "2":
                    service.displayDetailedProblems();
                    pressEnterToContinue();
                    break;
                case "3":
                    System.out.print("Difficulty (Easy/Medium/Hard): ");
                    service.getProblemsByDifficulty(scanner.nextLine().trim());
                    pressEnterToContinue();
                    break;
                case "4":
                    try {
                        System.out.print("Problem ID: ");
                        int pid = Integer.parseInt(scanner.nextLine().trim());
                        service.displayProblem(pid);
                    } catch (Exception e) {
                        System.out.println("[Error] Invalid ID.");
                    }
                    pressEnterToContinue();
                    break;
                default:
                    System.out.println("[Error] Invalid option.");
                    pressEnterToContinue();
            }
        }
    }

    private static void startMatchmaking() {
        System.out.println("\n--- BATTLE ARENA ---");
        try {
            System.out.println("Active Matches:");
            service.displayActiveMatches();
            System.out.println("\nCreating new match...");
            
            System.out.print("Match ID: ");
            int mId = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Player 1 ID: ");
            int p1Id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Player 2 ID: ");
            int p2Id = Integer.parseInt(scanner.nextLine().trim());
            System.out.print("Problem ID: ");
            int probId = Integer.parseInt(scanner.nextLine().trim());

            User p1Raw = service.findUserById(p1Id);
            User p2Raw = service.findUserById(p2Id);
            Problem prob = service.findProblemById(probId);

            // Detailed validation feedback
            if (p1Raw == null) {
                System.out.println("[Error] Player 1 (ID: " + p1Id + ") not found.");
                pressEnterToContinue(); return;
            }
            if (p2Raw == null) {
                System.out.println("[Error] Player 2 (ID: " + p2Id + ") not found.");
                pressEnterToContinue(); return;
            }
            if (prob == null) {
                System.out.println("[Error] Problem (ID: " + probId + ") not found.");
                pressEnterToContinue(); return;
            }
            if (p1Raw instanceof Admin || p2Raw instanceof Admin) {
                System.out.println("[Error] Matchmaking failed: Admins cannot participate in duels.");
                pressEnterToContinue(); return;
            }

            Match match = service.createMatch(mId, (RegularUser) p1Raw, (RegularUser) p2Raw, prob);
            if (match == null) {
                System.out.println("[Error] Match ID " + mId + " is already taken.");
                pressEnterToContinue();
                return;
            }
            
            System.out.println("\n--- LIVE DUEL: " + p1Raw.getUsername() + " VS " + p2Raw.getUsername() + " ---");
            System.out.println("TOPIC: " + prob.getTitle());
            System.out.println("--------------------------------------------------");
            System.out.print("Who submits? (1 for " + p1Raw.getUsername() + " / 2 for " + p2Raw.getUsername() + " / 0 to quit): ");
            String whoStr = scanner.nextLine().trim();
            
            if (!whoStr.equals("0")) {
                int who = Integer.parseInt(whoStr);
                RegularUser actor = (who == 1) ? (RegularUser)p1Raw : (RegularUser)p2Raw;
                System.out.print("Solution Code: ");
                String code = scanner.nextLine().trim();
                System.out.print("Passes all tests? (y/n): ");
                boolean correct = scanner.nextLine().trim().equalsIgnoreCase("y");
                service.submitCode(mId * 1000 + (int)(Math.random()*999), match, actor, code, correct);
            } else {
                System.out.println("[System] Duel paused.");
            }
            pressEnterToContinue();

        } catch (Exception e) {
            System.out.println("[Error] Matchmaking interrupted: " + e.getMessage());
            pressEnterToContinue();
        }
    }

    private static void showHistory() {
        System.out.print("\nEnter User ID: ");
        try {
            int id = Integer.parseInt(scanner.nextLine().trim());
            User u = service.findUserById(id);
            if (u instanceof RegularUser) {
                service.getUserHistory((RegularUser) u);
            } else {
                System.out.println("[Error] History only available for players.");
            }
        } catch (Exception e) {
            System.out.println("[Error] Invalid ID.");
        }
        pressEnterToContinue();
    }

    private static void showAnalytics() {
        service.displayPlatformStats();
        pressEnterToContinue();
    }
}
