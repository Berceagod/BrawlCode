package repositories;

import config.DatabaseConfiguration;
import users.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements GenericRepository<User> {
    private static UserRepository instance;
    private final Connection connection;

    private UserRepository() {
        this.connection = DatabaseConfiguration.getInstance().getConnection();
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    @Override
    public void create(User entity) {
        String query = "INSERT OR IGNORE INTO users (id, username, type, elo_rating, premium_badge, access_level) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, entity.getId());
            stmt.setString(2, entity.getUsername());
            
            if (entity instanceof Admin) {
                stmt.setString(3, "Admin");
                stmt.setNull(4, Types.INTEGER);
                stmt.setNull(5, Types.VARCHAR);
                stmt.setInt(6, ((Admin) entity).getAccessLevel());
            } else if (entity instanceof PremiumUser) {
                stmt.setString(3, "Premium");
                stmt.setInt(4, ((PremiumUser) entity).getEloRating());
                stmt.setString(5, ((PremiumUser) entity).getPremiumBadge());
                stmt.setNull(6, Types.INTEGER);
            } else if (entity instanceof RegularUser) {
                stmt.setString(3, "Regular");
                stmt.setInt(4, ((RegularUser) entity).getEloRating());
                stmt.setNull(5, Types.VARCHAR);
                stmt.setNull(6, Types.INTEGER);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User read(int id) {
        String query = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String type = rs.getString("type");
                String username = rs.getString("username");
                if (type.equals("Admin")) {
                    return new Admin(id, username, rs.getInt("access_level"));
                } else if (type.equals("Premium")) {
                    PremiumUser pu = new PremiumUser(id, username, rs.getString("premium_badge"));
                    pu.setEloRating(rs.getInt("elo_rating"));
                    return pu;
                } else if (type.equals("Regular")) {
                    RegularUser ru = new RegularUser(id, username);
                    ru.setEloRating(rs.getInt("elo_rating"));
                    return ru;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(User entity) {
        String query = "UPDATE users SET username = ?, elo_rating = ?, premium_badge = ?, access_level = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, entity.getUsername());
            if (entity instanceof Admin) {
                stmt.setNull(2, Types.INTEGER);
                stmt.setNull(3, Types.VARCHAR);
                stmt.setInt(4, ((Admin) entity).getAccessLevel());
            } else if (entity instanceof PremiumUser) {
                stmt.setInt(2, ((PremiumUser) entity).getEloRating());
                stmt.setString(3, ((PremiumUser) entity).getPremiumBadge());
                stmt.setNull(4, Types.INTEGER);
            } else if (entity instanceof RegularUser) {
                stmt.setInt(2, ((RegularUser) entity).getEloRating());
                stmt.setNull(3, Types.VARCHAR);
                stmt.setNull(4, Types.INTEGER);
            }
            stmt.setInt(5, entity.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> readAll() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                String username = rs.getString("username");
                if (type.equals("Admin")) {
                    users.add(new Admin(id, username, rs.getInt("access_level")));
                } else if (type.equals("Premium")) {
                    PremiumUser pu = new PremiumUser(id, username, rs.getString("premium_badge"));
                    pu.setEloRating(rs.getInt("elo_rating"));
                    users.add(pu);
                } else if (type.equals("Regular")) {
                    RegularUser ru = new RegularUser(id, username);
                    ru.setEloRating(rs.getInt("elo_rating"));
                    users.add(ru);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
