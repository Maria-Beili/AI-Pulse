package epaw.lab4.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import epaw.lab4.model.Tweet;

public class TweetRepository extends BaseRepository {

    private static TweetRepository instance;

    private TweetRepository() {
        super();
    }

    public static synchronized TweetRepository getInstance() {
        if (instance == null) {
            instance = new TweetRepository();
        }
        return instance;
    }
	
	public void save(Tweet tweet) {
		String query = "INSERT INTO tweets (uid,postdatetime,content,image,pid) VALUES (?,?,?,?,?)";
		try (PreparedStatement statement = db.prepareStatement(query)) {
			statement.setInt(1, tweet.getUid());
			statement.setTimestamp(2, tweet.getPostDateTime());
			statement.setString(3, tweet.getContent());
			statement.setString(4, tweet.getImage());
			if (tweet.getPid() == null) {
				statement.setNull(5, java.sql.Types.INTEGER);
			} else {
				statement.setInt(5, tweet.getPid());
			}
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void update(Integer id, Integer uid, String content, String image) {
		String query = "UPDATE tweets SET content = ?, image = ? WHERE id = ? AND uid = ?";
		try (PreparedStatement statement = db.prepareStatement(query)) {
			statement.setString(1, content);
			statement.setString(2, image);
			statement.setInt(3, id);
			statement.setInt(4, uid);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/* Delete existing tweet */
	public void delete(Integer id, Integer uid) {
		String query = "DELETE FROM tweets WHERE id = ? AND uid=?";
		try (PreparedStatement statement = db.prepareStatement(query)) {
			statement.setInt(1, id);
			statement.setInt(2, uid);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    public void deleteById(Integer id) {
        String query = "DELETE FROM tweets WHERE id = ?";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* Get tweets from a user given start and end */
    public Optional<List<Tweet>> findByUser(Integer uid, Integer start, Integer end) {
        List<Tweet> tweets = new ArrayList<Tweet>();
        String query = "SELECT tweets.id,tweets.uid,tweets.postdatetime,tweets.content,tweets.image,tweets.pid,users.name,users.picture,(SELECT COUNT(*) FROM likes WHERE tid = tweets.id) AS likesCount FROM tweets INNER JOIN users ON tweets.uid = users.id where tweets.uid = ? AND tweets.pid IS NULL ORDER BY tweets.postdatetime DESC LIMIT ?,? ;";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, uid);
            statement.setInt(2, start);
            statement.setInt(3, end);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Tweet tweet = new Tweet();
                    tweet.setId(rs.getInt("id"));
                    tweet.setUid(rs.getInt("uid"));
                    tweet.setPostDateTime(rs.getTimestamp("postdatetime"));
                    tweet.setContent(rs.getString("content"));
                    tweet.setImage(rs.getString("image"));
                    tweet.setPid((Integer) rs.getObject("pid"));
                    tweet.setUname(rs.getString("name"));
                    tweet.setUpicture(rs.getString("picture"));
                    tweet.setLikesCount(rs.getInt("likesCount"));
                    tweets.add(tweet);
                }
                return Optional.of(tweets);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<List<Tweet>> findLatest(Integer start, Integer end) {
		List<Tweet> tweets = new ArrayList<Tweet>();
		// INNER JOIN users: Avoids N+1 query problem by fetching user info eagerly.
		// (SELECT COUNT(*)...): Delegates aggregate math to SQLite for performance.
		// WHERE tweets.pid IS NULL: Filters out replies/comments, ensuring we only get root nodes.
		String query = "SELECT tweets.id,tweets.uid,tweets.postdatetime,tweets.content,tweets.image,tweets.pid,users.name,users.picture,(SELECT COUNT(*) FROM likes WHERE tid = tweets.id) AS likesCount FROM tweets INNER JOIN users ON tweets.uid = users.id WHERE tweets.pid IS NULL ORDER BY tweets.postdatetime DESC LIMIT ?,? ;";
		// PreparedStatement: Prevents SQL Injection by treating parameters (?) strictly as data, not executable code.
		try (PreparedStatement statement = db.prepareStatement(query)) {
			statement.setInt(1, start);
			statement.setInt(2, end);
			try (ResultSet rs = statement.executeQuery()) {
				while (rs.next()) {
					Tweet tweet = new Tweet();
					tweet.setId(rs.getInt("id"));
					tweet.setUid(rs.getInt("uid"));
					tweet.setPostDateTime(rs.getTimestamp("postdatetime"));
					tweet.setContent(rs.getString("content"));
					tweet.setImage(rs.getString("image"));
					tweet.setPid((Integer) rs.getObject("pid"));
					tweet.setUname(rs.getString("name"));
					tweet.setUpicture(rs.getString("picture"));
					tweet.setLikesCount(rs.getInt("likesCount"));
					tweets.add(tweet);
				}
				return Optional.of(tweets);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public Optional<List<Tweet>> findAllPosts(Integer start, Integer end) {
        List<Tweet> tweets = new ArrayList<Tweet>();
        String query = "SELECT tweets.id,tweets.uid,tweets.postdatetime,tweets.content,tweets.image,tweets.pid,users.name,users.picture,(SELECT COUNT(*) FROM likes WHERE tid = tweets.id) AS likesCount FROM tweets INNER JOIN users ON tweets.uid = users.id WHERE tweets.pid IS NULL ORDER BY tweets.postdatetime DESC LIMIT ?,? ;";
        try (PreparedStatement statement = db.prepareStatement(query)) {
            statement.setInt(1, start);
            statement.setInt(2, end);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Tweet tweet = new Tweet();
                    tweet.setId(rs.getInt("id"));
                    tweet.setUid(rs.getInt("uid"));
                    tweet.setPostDateTime(rs.getTimestamp("postdatetime"));
                    tweet.setContent(rs.getString("content"));
                    tweet.setImage(rs.getString("image"));
                    tweet.setUpicture(rs.getString("picture"));
                    tweet.setLikesCount(rs.getInt("likesCount"));
                    tweets.add(tweet);
                }
                return Optional.of(tweets);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<List<Tweet>> findFeedByUser(Integer uid, Integer start, Integer end) {
		List<Tweet> tweets = new ArrayList<Tweet>();
		String query = "SELECT t.id,t.uid,t.postdatetime,t.content,t.image,t.pid,u.name,u.picture,(SELECT COUNT(*) FROM likes WHERE tid = t.id) AS likesCount "
			+ "FROM tweets t INNER JOIN users u ON t.uid = u.id "
			+ "WHERE t.pid IS NULL AND (t.uid = ? OR t.uid IN (SELECT fid FROM follows WHERE uid = ?)) "
			+ "ORDER BY t.postdatetime DESC LIMIT ?,?;";
		try (PreparedStatement statement = db.prepareStatement(query)) {
			statement.setInt(1, uid);
			statement.setInt(2, uid);
			statement.setInt(3, start);
			statement.setInt(4, end);
			try (ResultSet rs = statement.executeQuery()) {
				while (rs.next()) {
					Tweet tweet = new Tweet();
					tweet.setId(rs.getInt("id"));
					tweet.setUid(rs.getInt("uid"));
					tweet.setPostDateTime(rs.getTimestamp("postdatetime"));
					tweet.setContent(rs.getString("content"));
					tweet.setImage(rs.getString("image"));
					tweet.setPid((Integer) rs.getObject("pid"));
					tweet.setUname(rs.getString("name"));
					tweet.setUpicture(rs.getString("picture"));
					tweet.setLikesCount(rs.getInt("likesCount"));
					tweets.add(tweet);
				}
				return Optional.of(tweets);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public Optional<List<Tweet>> findCommentsByParent(Integer pid, Integer start, Integer end) {
		List<Tweet> tweets = new ArrayList<Tweet>();
		String query = "SELECT t.id,t.uid,t.postdatetime,t.content,t.image,t.pid,u.name,u.picture,(SELECT COUNT(*) FROM likes WHERE tid = t.id) AS likesCount "
			+ "FROM tweets t INNER JOIN users u ON t.uid = u.id "
			+ "WHERE t.pid = ? ORDER BY t.postdatetime ASC LIMIT ?,?;";
		try (PreparedStatement statement = db.prepareStatement(query)) {
			statement.setInt(1, pid);
			statement.setInt(2, start);
			statement.setInt(3, end);
			try (ResultSet rs = statement.executeQuery()) {
				while (rs.next()) {
					Tweet tweet = new Tweet();
					tweet.setId(rs.getInt("id"));
					tweet.setUid(rs.getInt("uid"));
					tweet.setPostDateTime(rs.getTimestamp("postdatetime"));
					tweet.setContent(rs.getString("content"));
					tweet.setImage(rs.getString("image"));
					tweet.setPid((Integer) rs.getObject("pid"));
					tweet.setUname(rs.getString("name"));
					tweet.setUpicture(rs.getString("picture"));
					tweet.setLikesCount(rs.getInt("likesCount"));
					tweets.add(tweet);
				}
				return Optional.of(tweets);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public Optional<Tweet> findById(Integer id) {
		String query = "SELECT tweets.id,tweets.uid,tweets.postdatetime,tweets.content,tweets.image,tweets.pid,users.name,users.picture,(SELECT COUNT(*) FROM likes WHERE tid = tweets.id) AS likesCount FROM tweets INNER JOIN users ON tweets.uid = users.id WHERE tweets.id = ?;";
		try (PreparedStatement statement = db.prepareStatement(query)) {
			statement.setInt(1, id);
			try (ResultSet rs = statement.executeQuery()) {
				if (rs.next()) {
					Tweet tweet = new Tweet();
					tweet.setId(rs.getInt("id"));
					tweet.setUid(rs.getInt("uid"));
					tweet.setPostDateTime(rs.getTimestamp("postdatetime"));
					tweet.setContent(rs.getString("content"));
					tweet.setImage(rs.getString("image"));
					tweet.setPid((Integer) rs.getObject("pid"));
					tweet.setUname(rs.getString("name"));
					tweet.setUpicture(rs.getString("picture"));
					tweet.setLikesCount(rs.getInt("likesCount"));
					return Optional.of(tweet);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}

	public void addLike(Integer uid, Integer tid) {
		String query = "INSERT INTO likes (uid, tid) VALUES (?, ?)";
		// The (?) placeholders are filled safely by setInt below, protecting against SQL Injection
		try (PreparedStatement statement = db.prepareStatement(query)) {
			statement.setInt(1, uid);
			statement.setInt(2, tid);
			statement.executeUpdate();
		} catch (SQLException e) {
			// ignore if already liked
		}
	}

	public void removeLike(Integer uid, Integer tid) {
		String query = "DELETE FROM likes WHERE uid = ? AND tid = ?";
		try (PreparedStatement statement = db.prepareStatement(query)) {
			statement.setInt(1, uid);
			statement.setInt(2, tid);
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean isLikedBy(Integer tid, Integer uid) {
		if (uid == null) return false;
		String query = "SELECT 1 FROM likes WHERE tid = ? AND uid = ?";
		try (PreparedStatement statement = db.prepareStatement(query)) {
			statement.setInt(1, tid);
			statement.setInt(2, uid);
			try (ResultSet rs = statement.executeQuery()) {
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

}

