package epaw.lab4.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.stream.Collectors;

public class DBManager {

	private static DBManager instance;
	private Connection connection = null;
	private static final String DB_FILE = "lab4.db";

	private DBManager() {
		try {
			// SQLite connection
			Class.forName("org.sqlite.JDBC");
			boolean dbExists = Files.exists(Paths.get(DB_FILE));
			connection = DriverManager.getConnection("jdbc:sqlite:" + DB_FILE);

			// Enable foreign keys in SQLite
			try (Statement stmt = connection.createStatement()) {
				stmt.execute("PRAGMA foreign_keys = ON;");
			}

			if (!dbExists) {
				initDatabase();
			}
			ensureUserColumns();
			ensureTweetColumns();
			ensureLikesTable();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized DBManager getInstance() {
		if (instance == null) {
			instance = new DBManager();
		}
		return instance;
	}

	private void initDatabase() throws Exception {
		String schemaPath = "DB.txt";
		try (BufferedReader reader = new BufferedReader(new FileReader(schemaPath))) {
			String schema = reader.lines().collect(Collectors.joining("\n"));
			String[] statements = schema.split(";");
			try (Statement stmt = connection.createStatement()) {
				for (String sql : statements) {
					if (!sql.trim().isEmpty()) {
						stmt.execute(sql);
					}
				}
			}
		}
	}

	private void ensureUserColumns() {
		try (Statement stmt = connection.createStatement()) {
			if (!columnExists("users", "email")) {
				stmt.execute("ALTER TABLE users ADD COLUMN email VARCHAR(120)");
			}
			if (!columnExists("users", "phone")) {
				stmt.execute("ALTER TABLE users ADD COLUMN phone VARCHAR(20)");
			}
			if (!columnExists("users", "birth_date")) {
				stmt.execute("ALTER TABLE users ADD COLUMN birth_date VARCHAR(20)");
			}
			if (!columnExists("users", "role")) {
				stmt.execute("ALTER TABLE users ADD COLUMN role VARCHAR(20) DEFAULT 'REGULAR'");
			}

			stmt.execute("UPDATE users SET role='REGULAR' WHERE role IS NULL");
			stmt.execute("CREATE UNIQUE INDEX IF NOT EXISTS idx_users_email ON users(email)");
			stmt.execute("CREATE UNIQUE INDEX IF NOT EXISTS idx_users_phone ON users(phone)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void ensureTweetColumns() {
		try (Statement stmt = connection.createStatement()) {
			if (!columnExists("tweets", "image")) {
				stmt.execute("ALTER TABLE tweets ADD COLUMN image VARCHAR(255)");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void ensureLikesTable() {
		try (Statement stmt = connection.createStatement()) {
			stmt.execute("CREATE TABLE IF NOT EXISTS likes (" +
					"uid INTEGER NOT NULL, " +
					"tid INTEGER NOT NULL, " +
					"PRIMARY KEY (uid, tid), " +
					"FOREIGN KEY (uid) REFERENCES users (id) ON DELETE CASCADE, " +
					"FOREIGN KEY (tid) REFERENCES tweets (id) ON DELETE CASCADE)");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean columnExists(String tableName, String columnName) {
		String query = "PRAGMA table_info(" + tableName + ")";
		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
			while (rs.next()) {
				if (columnName.equalsIgnoreCase(rs.getString("name"))) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public PreparedStatement prepareStatement(String query) throws SQLException {
		return connection.prepareStatement(query);
	}

	public void close() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}