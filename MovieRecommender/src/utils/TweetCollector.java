package utils;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import main.SVMRecommender;

public class TweetCollector {
	
	public TweetCollector() {
		
		slangWords = new String[5470];
		slangWordMeanings = new String[5470];
		
	}
	
	String[] slangWords;
	String[] slangWordMeanings;
	
	public static void main(String[]args) {
		
		TweetCollector tweety = new TweetCollector();
		
		tweety.gatherTweets("IOS");
	}
	
	
	
	public boolean gatherTweets(String keyword) {
		
		JazzySpellChecker jazzySpellChecker = new JazzySpellChecker();
		Replacer replacer = new Replacer();
		String[] SlangDictionary = {""};
		AccessToken accessToken = new AccessToken("213230083-lHi9RM3B6UNC1ZnlZeMUKq9BSscAPtwJjA7hA0J0", "SsnPms1WKuld3OkPqjzLMsZGryUhTjMUtgCAT9jyD7w");
		
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer("UQh36N88nkL620m1BjDhg", "QS1rVZInMBu5Ff2OC8P3NaTZchWBXG5smc4PEuYJUs");
		twitter.setOAuthAccessToken(accessToken);
		try {
	        Query query = new Query(keyword);
	        query.setCount(100);
	        query.lang("en");
	        QueryResult result;
	        
	        int i = 0;
	        do {
	        result = twitter.search(query);
	        List<Status> tweets = result.getTweets();
	        for (Status tweet : tweets) {
	        	i++;
	            System.out.println(i + /*"  @" + tweet.getUser().getScreenName() +*/ " - " + tweet.getText());
	            
	            String noSlangTweet = replacer.replaceAllWords(tweet.getText());
	            
	            String spellCheckedTweet = jazzySpellChecker.getCorrectedLine(noSlangTweet);
	            
	            System.out.println(i + /*"  @" + tweet.getUser().getScreenName() +*/ " - " + noSlangTweet);
	            
	            System.out.println(i + /*"  @" + tweet.getUser().getScreenName() +*/ " - " + spellCheckedTweet);
	            
	            
	        }
	        }while((query = result.nextQuery()) != null);
	    }
	    catch (TwitterException te) {
	        te.printStackTrace();
	        System.out.println("Failed to search tweets: " + te.getMessage());
	        return false;
	    }
		
		return true;
	}
	
	
	
	
	
}
