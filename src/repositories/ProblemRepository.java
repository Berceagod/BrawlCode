package repositories;

import config.DatabaseConfiguration;
import problems.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProblemRepository implements GenericRepository<Problem> {
    private static ProblemRepository instance;
    private final Connection connection;

    private ProblemRepository() {
        this.connection = DatabaseConfiguration.getInstance().getConnection();
    }

    public static ProblemRepository getInstance() {
        if (instance == null) {
            instance = new ProblemRepository();
        }
        return instance;
    }

    @Override
    public void create(Problem entity) {
        String query = "INSERT OR IGNORE INTO problems (id, title, difficulty, type, time_limit, schema_name) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, entity.getId());
            stmt.setString(2, entity.getTitle());
            stmt.setString(3, entity.getDifficulty());
            if (entity instanceof AlgorithmicProblem) {
                stmt.setString(4, "Algorithmic");
                stmt.setDouble(5, ((AlgorithmicProblem) entity).getTimeLimit());
                stmt.setNull(6, Types.VARCHAR);
            } else if (entity instanceof DatabaseProblem) {
                stmt.setString(4, "Database");
                stmt.setNull(5, Types.DOUBLE);
                stmt.setString(6, ((DatabaseProblem) entity).getSchemaName());
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Problem read(int id) {
        String query = "SELECT * FROM problems WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String type = rs.getString("type");
                String title = rs.getString("title");
                String difficulty = rs.getString("difficulty");
                if (type.equals("Algorithmic")) {
                    return new AlgorithmicProblem(id, title, difficulty, rs.getDouble("time_limit"));
                } else if (type.equals("Database")) {
                    return new DatabaseProblem(id, title, difficulty, rs.getString("schema_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Problem entity) {
        String query = "UPDATE problems SET title = ?, difficulty = ?, time_limit = ?, schema_name = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, entity.getTitle());
            stmt.setString(2, entity.getDifficulty());
            if (entity instanceof AlgorithmicProblem) {
                stmt.setDouble(3, ((AlgorithmicProblem) entity).getTimeLimit());
                stmt.setNull(4, Types.VARCHAR);
            } else if (entity instanceof DatabaseProblem) {
                stmt.setNull(3, Types.DOUBLE);
                stmt.setString(4, ((DatabaseProblem) entity).getSchemaName());
            }
            stmt.setInt(5, entity.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM problems WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Problem> readAll() {
        List<Problem> problems = new ArrayList<>();
        String query = "SELECT * FROM problems";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                String title = rs.getString("title");
                String difficulty = rs.getString("difficulty");
                if (type.equals("Algorithmic")) {
                    problems.add(new AlgorithmicProblem(id, title, difficulty, rs.getDouble("time_limit")));
                } else if (type.equals("Database")) {
                    problems.add(new DatabaseProblem(id, title, difficulty, rs.getString("schema_name")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return problems;
    }
}
