package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseConfiguration {

    private static DatabaseConfiguration instance;
    private Connection connection;
    private static final String URL = "jdbc:sqlite:brawlcode.db";

    private DatabaseConfiguration() {
        try {
            connection = DriverManager.getConnection(URL);
            initDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static DatabaseConfiguration getInstance() {
        if (instance == null) {
            instance = new DatabaseConfiguration();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void initDatabase() {
        try {
            String schema = new String(Files.readAllBytes(Paths.get("schema.sql")));
            String[] queries = schema.split(";");
            for (String query : queries) {
                if (!query.trim().isEmpty()) {
                    try (Statement stmt = connection.createStatement()) {
                        stmt.execute(query.trim());
                    }
                }
            }
            System.out.println("Database initialized successfully.");
        } catch (Exception e) {
            System.out.println("Error initializing database schema.");
            e.printStackTrace();
        }
    }
}
