# ⚔️ BrawlCode - The Ultimate Competitive Arena

BrawlCode is a professional Java-based CLI application designed to manage a competitive programming platform. It allows users to register, solve algorithmic and database problems, compete in duels, and track their progress through a global leaderboard.

---

## 🚀 Key Features

*   **Advanced User Management:** Support for Regular, Premium, and Admin users with specialized attributes (ELO rating, badges, access levels).
*   **Problem Archive:** A diverse collection of Algorithmic and Database problems, filterable by difficulty and category.
*   **Battle Arena (Matchmaking):** Real-time duel simulation between players with automated ELO updates based on performance.
*   **Global Leaderboard:** A dynamic, sorted ranking system for the top players.
*   **Platform Analytics:** Real-time statistics on users, problems, and active matches.
*   **Audit Logging:** Every critical action is recorded in a `audit.csv` file with timestamps for transparency.
*   **Persistent Storage:** Robust data management using SQLite and JDBC.

---

## 🏗️ Architecture

The project follows a **Three-Tier Architecture** to ensure maintainability and scalability:

1.  **Presentation Layer (`Main.java`):** A professional CLI that handles user input, validation, and visual output.
2.  **Service Layer (`PlatformService`, `AuditService`):** The "brain" of the application where business rules and logic are orchestrated.
3.  **Data Access Layer (`Repositories`):** A bridge between Java objects and the SQLite database using the Singleton and Repository patterns.

---

## 🧩 OOP Principles Applied

*   **Encapsulation:** All models use private/protected fields with controlled access through getters/setters.
*   **Inheritance:** Hierarchies for Users (`User` -> `RegularUser` -> `PremiumUser`) and Problems (`Problem` -> `AlgorithmicProblem`).
*   **Polymorphism:** Abstract methods like `getProblemDetails()` allow different problem types to be handled uniformly.
*   **Generics:** A `GenericRepository<T>` interface defines standard CRUD operations for all entities.
*   **Singleton Pattern:** Used in `DatabaseConfiguration`, `AuditService`, and all Repositories to manage shared resources efficiently.

---

## 🛠️ Technical Stack

*   **Language:** Java 17+
*   **Database:** SQLite (Relational)
*   **Persistence:** JDBC (Java Database Connectivity)
*   **Logging:** Custom CSV-based Audit Service
*   **Format:** GitHub Flavored Markdown for documentation

---

## 🏁 How to Run

1.  Ensure you have **Java** and the **SQLite JDBC Driver** in your classpath.
2.  The database is automatically initialized using `schema.sql` on the first run.
3.  Compile and run `Main.java`.

---

## 📊 Database Schema

The system uses 4 main tables:
*   `users`: Stores all user types and their specific stats.
*   `problems`: Stores both algorithmic and database-related problems.
*   `matches`: Tracks confrontations between players.
*   `submissions`: Records code submissions and their correctness.

---
*Created as a high-performance, educational Java project.*
