package repositories;

import config.DatabaseConfiguration;
import matches.Match;
import problems.Problem;
import users.RegularUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatchRepository implements GenericRepository<Match> {
    private static MatchRepository instance;
    private final Connection connection;

    private MatchRepository() {
        this.connection = DatabaseConfiguration.getInstance().getConnection();
    }

    public static MatchRepository getInstance() {
        if (instance == null) {
            instance = new MatchRepository();
        }
        return instance;
    }

    @Override
    public void create(Match entity) {
        String query = "INSERT OR IGNORE INTO matches (id, player1_id, player2_id, problem_id, is_finished) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, entity.getMatchId());
            stmt.setInt(2, entity.getPlayer1().getId());
            stmt.setInt(3, entity.getPlayer2().getId());
            stmt.setInt(4, entity.getProblem().getId());
            stmt.setInt(5, entity.isFinished() ? 1 : 0);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Match read(int id) {
        String query = "SELECT * FROM matches WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                RegularUser p1 = (RegularUser) UserRepository.getInstance().read(rs.getInt("player1_id"));
                RegularUser p2 = (RegularUser) UserRepository.getInstance().read(rs.getInt("player2_id"));
                Problem prob = ProblemRepository.getInstance().read(rs.getInt("problem_id"));
                Match match = new Match(id, p1, p2, prob);
                match.setFinished(rs.getInt("is_finished") == 1);
                return match;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Match entity) {
        String query = "UPDATE matches SET is_finished = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, entity.isFinished() ? 1 : 0);
            stmt.setInt(2, entity.getMatchId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM matches WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Match> readAll() {
        List<Match> matches = new ArrayList<>();
        String query = "SELECT * FROM matches";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                RegularUser p1 = (RegularUser) UserRepository.getInstance().read(rs.getInt("player1_id"));
                RegularUser p2 = (RegularUser) UserRepository.getInstance().read(rs.getInt("player2_id"));
                Problem prob = ProblemRepository.getInstance().read(rs.getInt("problem_id"));
                Match match = new Match(id, p1, p2, prob);
                match.setFinished(rs.getInt("is_finished") == 1);
                matches.add(match);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return matches;
    }
}
