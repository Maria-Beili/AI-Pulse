package epaw.lab4.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import epaw.lab4.model.User;

public class UserRepository extends BaseRepository {

    private static UserRepository instance;

    private UserRepository() {
        super();
    }

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public boolean existsByUsername(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE name = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByUsernameExceptId(String username, Integer id) {
        String query = "SELECT COUNT(*) FROM users WHERE name = ? AND id <> ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setInt(2, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByEmail(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByEmailExceptId(String email, Integer id) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ? AND id <> ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, email);
            statement.setInt(2, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByPhone(String phone) {
        String query = "SELECT COUNT(*) FROM users WHERE phone = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, phone);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsByPhoneExceptId(String phone, Integer id) {
        String query = "SELECT COUNT(*) FROM users WHERE phone = ? AND id <> ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, phone);
            statement.setInt(2, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkLogin(User user) {
        String query = "SELECT id, picture, email, phone, birth_date, role FROM users WHERE name=? AND password=?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    user.setId(rs.getInt("id"));
                    user.setPicture(rs.getString("picture"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setBirthDate(rs.getString("birth_date"));
                    user.setRole(rs.getString("role"));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void save(User user) {
        if (user.getRole() == null) {
            user.setRole("REGULAR");
        }
        String query = "INSERT INTO users (name, email, phone, birth_date, password, picture, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            statement.setString(4, user.getBirthDate());
            statement.setString(5, user.getPassword());
            statement.setString(6, user.getPicture());
            statement.setString(7, user.getRole());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<User> findByName(String name) {
        String query = "SELECT id, name, email, phone, birth_date, password, picture, role FROM users WHERE name = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setPhone(rs.getString("phone"));
                user.setBirthDate(rs.getString("birth_date"));
                user.setPassword(rs.getString("password"));
                user.setPicture(rs.getString("picture"));
                user.setRole(rs.getString("role"));
                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    // Find a user by their name
    public Optional<User> findById(Integer id) {
        String query = "SELECT id, name, email, phone, birth_date, password, picture, role FROM users WHERE id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setBirthDate(rs.getString("birth_date"));
                    user.setPassword(rs.getString("password"));
                    user.setPicture(rs.getString("picture"));
                    user.setRole(rs.getString("role"));
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    
    // Retrieve all users from the database
    public Optional<List<User>> findAll() {
        List<User> users = new ArrayList<>();
        String query = "SELECT id, name, email, phone, birth_date, picture, role FROM users";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setEmail(rs.getString("email"));
                    user.setPhone(rs.getString("phone"));
                    user.setBirthDate(rs.getString("birth_date"));
                    user.setPicture(rs.getString("picture"));
                    user.setRole(rs.getString("role"));
                    users.add(user);
                }
                return Optional.of(users);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void update(User user) {
        if (user.getRole() == null) {
            user.setRole("REGULAR");
        }
        String query = "UPDATE users SET name = ?, email = ?, phone = ?, birth_date = ?, password = ?, picture = ?, role = ? WHERE id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPhone());
            statement.setString(4, user.getBirthDate());
            statement.setString(5, user.getPassword());
            statement.setString(6, user.getPicture());
            statement.setString(7, user.getRole());
            statement.setInt(8, user.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
	// Follow a user
	public void followUser(Integer uid, Integer fid) {
		String query = "INSERT INTO follows (uid,fid) VALUES (?,?)";
		try (PreparedStatement statement = db.prepareStatement(query)) {
			statement.setInt(1, uid);
			statement.setInt(2, fid);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Unfollow a user
	public void unfollowUser(Integer uid, Integer fid) {
		String query = "DELETE FROM follows WHERE uid = ? AND fid = ?";
		try (PreparedStatement statement = db.prepareStatement(query)) {
			statement.setInt(1, uid);
			statement.setInt(2, fid);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}    
    
	public Optional<List<User>> findNotFollowed(Integer id, Integer start, Integer end) {
        String query = "SELECT u.id,u.name,u.picture FROM users u "
                + "LEFT JOIN follows fs ON u.id = fs.fid "
                + "WHERE u.id NOT IN (SELECT fid FROM follows WHERE uid = ?) AND u.id <> ? "
                + "GROUP BY u.id,u.name,u.picture "
                + "ORDER BY COUNT(fs.uid) DESC, u.name ASC LIMIT ?,?;";
		try (PreparedStatement statement = db.prepareStatement(query)) {
			statement.setInt(1, id);
			statement.setInt(2, id);
			statement.setInt(3, start);
			statement.setInt(4, end);
			try (ResultSet rs = statement.executeQuery()) {
				List<User> users = new ArrayList<User>();
				while (rs.next()) {
					User user = new User();
					user.setId(rs.getInt("id"));
					user.setName(rs.getString("name"));
					user.setPicture(rs.getString("picture"));
					users.add(user);
				}
				return Optional.of(users);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public Optional<List<User>> findFollowed(Integer id, Integer start, Integer end) {
		String query = "SELECT id,name,picture FROM users,follows WHERE id = fid AND uid = ? ORDER BY name LIMIT ?,?;";
		try (PreparedStatement statement = db.prepareStatement(query)) {
			statement.setInt(1, id);
			statement.setInt(2, start);
			statement.setInt(3, end);
			try (ResultSet rs = statement.executeQuery()) {
				List<User> users = new ArrayList<User>();
				while (rs.next()) {
					User user = new User();
					user.setId(rs.getInt("id"));
					user.setName(rs.getString("name"));
					user.setPicture(rs.getString("picture"));
					users.add(user);
				}
				return Optional.of(users);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

    public void deleteById(Integer id) {
        String query = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<List<User>> findFollowers(Integer id, Integer start, Integer end) {
        String query = "SELECT u.id,u.name,u.picture FROM users u "
                + "INNER JOIN follows f ON u.id = f.uid "
                + "WHERE f.fid = ? ORDER BY u.name LIMIT ?,?;";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.setInt(2, start);
            statement.setInt(3, end);
            try (ResultSet rs = statement.executeQuery()) {
                List<User> users = new ArrayList<User>();
                while (rs.next()) {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setPicture(rs.getString("picture"));
                    users.add(user);
                }
                return Optional.of(users);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}