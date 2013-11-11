package utils;

import gui.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import com.rapidminer.operator.learner.functions.kernel.jmysvm.svm.SVMregression;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import main.SVMRecommender;

public class TweetCollector {

	private Logger logger;

	public TweetCollector(Logger logger) {

		slangWords = new String[5470];
		slangWordMeanings = new String[5470];
		this.logger = logger;
	}

	String[] slangWords;
	String[] slangWordMeanings;

	public boolean gatherTweets(String keyword) {

		// JazzySpellChecker jazzySpellChecker = new JazzySpellChecker();
		// Replacer replacer = new Replacer();
		String[] SlangDictionary = { "" };
		AccessToken accessToken = new AccessToken(
				"213230083-lHi9RM3B6UNC1ZnlZeMUKq9BSscAPtwJjA7hA0J0",
				"SsnPms1WKuld3OkPqjzLMsZGryUhTjMUtgCAT9jyD7w");

		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer("UQh36N88nkL620m1BjDhg",
				"QS1rVZInMBu5Ff2OC8P3NaTZchWBXG5smc4PEuYJUs");
		twitter.setOAuthAccessToken(accessToken);
		try {
			Query query = new Query(keyword);
			query.setCount(100);
			query.lang("en");
			QueryResult result;

			logger.clearStats();
			int i = 0;
			do {
				result = twitter.search(query);
				List<Status> tweets = result.getTweets();
				for (Status tweet : tweets) {
					i++;
					// String noSlangTweet = replacer.replaceAllWords(tweet
					// .getText());
					//
					// String spellCheckedTweet = jazzySpellChecker
					// .getCorrectedLine(noSlangTweet);
					String label = SVMRecommender.rapidminer
							.classify(SVMRecommender.parser.parseText(tweet
									.getText()));
					int value = 0;
					if (label.equals("pos"))
						value = 1;
					else if (label.equals("neg"))
						value = -1;
					logger.updateStats(value);
				}
			} while ((query = result.nextQuery()) != null);
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
			return false;
		}

		return true;
	}

}
