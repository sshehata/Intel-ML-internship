package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.rapidminer.tools.container.Pair;
import com.rapidminer.tools.jep.function.expressions.text.Replace;

/*
 * Author: Samy Shihata
 * Parses text and applies relevant preprocessing
 */

public class Parser extends Thread {
	private ConcurrentLinkedQueue<Pair<String, Integer>> writingQueue;
	private boolean doneParsing;
	private File posDir;
	private File negDir;
	private JazzySpellChecker jazzySpellChecker;
	private Replacer replacer;

	private boolean negate;
	
	static String[] spamFilter = {
		"http"
	};

	public Parser() {
		writingQueue = new ConcurrentLinkedQueue<>();
//		jazzySpellChecker = new JazzySpellChecker();
//		replacer = new Replacer();
	}

	public void parseTrainingData() {
		// Prepare temp training destination
		System.out.print("Parsing training data");
		posDir = new File("resources/temp/training set/pos/");
		negDir = new File("resources/temp/training set/neg/");
		posDir.mkdirs();
		negDir.mkdirs();

		// Parse POS and NEG files and add them to the writing queue
		File posBaseDir = new File("resources/training set/pos");
		File negBaseDir = new File("resources/training set/neg");
		doneParsing = false;
		this.start();
		for (File file : posBaseDir.listFiles()) {
			writingQueue.add(new Pair<String, Integer>(parseFile(file), 1));
		}
		for (File file : negBaseDir.listFiles()) {
			writingQueue.add(new Pair<String, Integer>(parseFile(file), -1));
		}

		// Wait until the writing queue is done
		doneParsing = true;
		while (this.isAlive()) {
			try {
				sleep(10);
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}
		writingQueue = null; // Free the writing queue when done
	}
	
	/*
	 * Author: Maged Shalaby
	 * Checks against list of predefined spam words and returns true if any of them are found
	 */
	
	private boolean filterSpam(String line){
		for(int i=0; i<spamFilter.length; i++)
			if(line.contains(spamFilter[i])) return true;
		
		return false;
	}
	
	
	public String parseFile(File file) {
		try {
			FileReader inStream = new FileReader(file);
			BufferedReader reader = new BufferedReader(inStream);


			String line = reader.readLine();
			if(line.contains(":D")) line+= " great";
			String newLine = "";
			negate = false;
			while (line != null) {
//				line = replacer.replaceAllWords(line);
//				line = jazzySpellChecker.getCorrectedLine(line);
				StringTokenizer tokenizer = new StringTokenizer(line);
				while (tokenizer.hasMoreTokens()) {
					String word = tokenizer.nextToken();
					word = negate(word);
					newLine += word + " ";
				}
				newLine += "\n";
				line = reader.readLine();
			}
			reader.close();
			return newLine;
		} catch (IOException e) {
			System.out.println(e);
			return null;
		}
	}

	/*
	 * Author: Samy Shihata
	 * Returns text of tweet to be classified after preprocessing
	 * Please merge into parseFile
	 */
	
	public String parseText(String text) {
		BufferedReader reader = new BufferedReader(new StringReader(text));
		try {
			String line = reader.readLine();
			if(filterSpam(line)) return null;
			if(line.contains(":D")) line+= " great";
			String newLine = "";
			negate = false;
			while (line != null) {
				StringTokenizer tokenizer = new StringTokenizer(line);
				while (tokenizer.hasMoreTokens()) {
					String word = tokenizer.nextToken();
					word = negate(word);
					newLine += word + " ";
				}
				newLine += "\n";
				line = reader.readLine();
			}
			reader.close();
			return newLine;
		} catch (IOException e) {
			System.out.println(e);
			return null;
		}
	}

	private String negate(String word) {
		if (word.equals("not") || word.contains("n't")) {
			negate = true;
		} else if (word.matches("[!,.\"]")) {
			negate = false;
		} else {
			if (negate)
				word = "NOT_" + word;
		}
		return word;
	}

	// Separate thread to write the files in the writing queue to disk
	public void run() {
		int i = 0;
		while (!doneParsing || !writingQueue.isEmpty()) {
			if (writingQueue.isEmpty()) {
				try {
					sleep(1);
					continue;
				} catch (InterruptedException e) {
					System.out.println(e);
				}
			}
			Pair<String, Integer> p = writingQueue.remove();
			try {
				FileWriter writer = new FileWriter(
						((Integer) (p.getSecond()) == 1 ? posDir.getPath()
								: negDir.getPath()) + "/" + i);
				writer.write((String) p.getFirst());
				writer.flush();
				writer.close();

				if (i % 500 == 0)
					System.out.print(".");
				if (i % 1000 == 0)
					System.out.print(i);
				i++;
			} catch (IOException e) {
				System.out.println(e);
			}
		}
		System.out.println("done. (" + i + " examples)");
	}

	public void close() {
		closeHelper(new File("resources/temp"));
	}

	private void closeHelper(File file) {
		if (file.isDirectory())
			for (File subFile : file.listFiles())
				closeHelper(subFile);
		file.delete();
	}
}
