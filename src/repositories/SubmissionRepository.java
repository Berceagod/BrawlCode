package repositories;

import config.DatabaseConfiguration;
import matches.Submission;
import users.RegularUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SubmissionRepository implements GenericRepository<Submission> {
    private static SubmissionRepository instance;
    private final Connection connection;

    private SubmissionRepository() {
        this.connection = DatabaseConfiguration.getInstance().getConnection();
    }

    public static SubmissionRepository getInstance() {
        if (instance == null) {
            instance = new SubmissionRepository();
        }
        return instance;
    }

    @Override
    public void create(Submission entity) {
        String query = "INSERT OR IGNORE INTO submissions (id, user_id, code_snippet, is_correct) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, entity.getSubmissionId());
            stmt.setInt(2, entity.getUser().getId());
            stmt.setString(3, entity.getCodeSnippet());
            stmt.setInt(4, entity.isCorrect() ? 1 : 0);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Submission read(int id) {
        String query = "SELECT * FROM submissions WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                RegularUser user = (RegularUser) UserRepository.getInstance().read(rs.getInt("user_id"));
                return new Submission(id, user, rs.getString("code_snippet"), rs.getInt("is_correct") == 1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Submission entity) {
        String query = "UPDATE submissions SET code_snippet = ?, is_correct = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, entity.getCodeSnippet());
            stmt.setInt(2, entity.isCorrect() ? 1 : 0);
            stmt.setInt(3, entity.getSubmissionId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM submissions WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Submission> readAll() {
        List<Submission> submissions = new ArrayList<>();
        String query = "SELECT * FROM submissions";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                RegularUser user = (RegularUser) UserRepository.getInstance().read(rs.getInt("user_id"));
                submissions.add(new Submission(id, user, rs.getString("code_snippet"), rs.getInt("is_correct") == 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return submissions;
    }
}
