package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.*;

public class Replacer {

	String[] slangWords;
	String[] slangWordMeanings;
	private Pattern p;

	public Replacer() {
		slangWords = new String[5470];
		slangWordMeanings = new String[5470];
		initializeDictionaries();
		String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
		p = Pattern.compile(urlPattern, Pattern.CASE_INSENSITIVE);
	}

	public boolean initializeDictionaries() {

		try {
			FileReader fReader = new FileReader(
					"resources/dictionary/slangDictionary.txt");
			BufferedReader breader = new BufferedReader(fReader);

			String[] tempLineSplitter = new String[2];
			String currentLine = "";
			int i = 0;
			while ((currentLine = breader.readLine()) != null) {
				tempLineSplitter = currentLine.split(",");
				slangWords[i] = "\\s*\\b" + tempLineSplitter[0] + "\\b\\s*";
				slangWordMeanings[i] = "\\s*\\b" + tempLineSplitter[1]
						+ "\\b\\s*";
				i++;
			}
			breader.close();
			fReader.close();

			// System.out.println(slangWords[35]);
			// System.out.println(slangWordMeanings[35]);
		}

		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public String replaceAllWords(String text) {

		String result = "";

		result = text.replaceAll("@\\p{L}+", "");
		result = result.replaceAll("#", "");
		result = result.replaceAll("\\n", "");
		result = RemoveUrl(result);
		result = StringUtils.replaceEach(result, slangWords, slangWordMeanings);
		return result;
	}

	private String RemoveUrl(String commentstr) {
		String commentstr1 = commentstr;
		
		Matcher m = p.matcher(commentstr1);
		return m.replaceAll("").trim();
//		
//		int i = 0;
//		while (m.find()) {
//			commentstr1 = commentstr1.replaceAll(m.group(i), "").trim();
//			i++;
//		}
//		return commentstr1;
	}

	public static void main(String[] args) {
		String text = "The stupid noob \n jumped over \n the candle";
		String[] searchList = { "stupid", "candle", "noob" };
		String[] replacementList = { "retarded", "bonfire", "noobzilla" };
		Replacer replacer = new Replacer();
		System.out.println(replacer.replaceAllWords(text));
	}

}
