package epaw.lab4.service;

import epaw.lab4.repository.TweetRepository;
import java.util.List;
import java.util.Optional;
import epaw.lab4.model.Tweet;

public class TweetService {
	
	private static TweetService instance;
	private TweetRepository tweetRepository;
	
	private TweetService() {
        this.tweetRepository = TweetRepository.getInstance();
    }
	
	public static synchronized TweetService getInstance() {
		if (instance == null) {
			instance = new TweetService();
		}
		return instance;
	}
	
	public void add(Tweet tweet) {
		tweetRepository.save(tweet);	
	}

	public void update(Integer id, Integer uid, String content, String image) {
		tweetRepository.update(id, uid, content, image);
	}
	
	public void delete(Integer id, Integer uid) {
		tweetRepository.delete(id, uid);
	}

    public void deleteById(Integer id) {
        tweetRepository.deleteById(id);
    }

    public List<Tweet> getAllTweets(Integer start, Integer end) {
        Optional<List<Tweet>> tweets = tweetRepository.findAllPosts(start,end);
        if (tweets.isPresent())
            return tweets.get();
        return null;
    }

    public List<Tweet> getLatestTweets(Integer start, Integer end) {
		Optional<List<Tweet>> tweets = tweetRepository.findLatest(start,end);
		if (tweets.isPresent())
			return tweets.get();
		return null;
	}

    public List<Tweet> getTweetsByUser(Integer uid, Integer start, Integer end) {
        Optional<List<Tweet>> tweets = tweetRepository.findByUser(uid, start, end);
        if (tweets.isPresent())
            return tweets.get();
        return null;
    }

    public List<Tweet> getFeedTweets(Integer uid, Integer start, Integer end) {
        Optional<List<Tweet>> tweets = tweetRepository.findFeedByUser(uid, start, end);
        if (tweets.isPresent())
            return tweets.get();
        return null;
    }

	public List<Tweet> getCommentsByParent(Integer pid, Integer start, Integer end) {
		Optional<List<Tweet>> tweets = tweetRepository.findCommentsByParent(pid, start, end);
		if (tweets.isPresent())
			return tweets.get();
		return null;
	}

	public Tweet getTweetById(Integer id) {
		Optional<Tweet> tweet = tweetRepository.findById(id);
		if (tweet.isPresent())
			return tweet.get();
		return null;
	}

	public boolean isLikedBy(Integer tid, Integer uid) {
		return tweetRepository.isLikedBy(tid, uid);
	}

	public void addLike(Integer uid, Integer tid) {
		tweetRepository.addLike(uid, tid);
	}

	public void removeLike(Integer uid, Integer tid) {
		tweetRepository.removeLike(uid, tid);
	}

}