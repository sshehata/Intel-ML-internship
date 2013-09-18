/**
 * Movie recommender app based on the SVM model.
 * @author Samy Shihata 
 */

package main;

import gui.Frame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import models.SVMModel;
import utils.FileParser;
import utils.RapidMinerInterface;

import com.rapidminer.example.set.SimpleExampleSet;

public class SVMRecommender {
	private static SVMModel model;
	private static RapidMinerInterface rapidminer;
	private static Frame frame;
	private static FileParser fileParser;

	public static void main(String... args) {
		init();
	}

	public static void init() {
		frame = new Frame();
		rapidminer = new RapidMinerInterface(frame.getLogger(),
				"resources/config/cleaning_training_data.xml", "resources/config/cleaning_file.xml",
				"resources/config/cleaning_text.xml");
		fileParser = new FileParser();
		fileParser.parseTrainingData();
		fileParser.parseTestingData();
		SimpleExampleSet cleanedData = rapidminer.cleanTrainingData();
		model = new SVMModel(frame.getLogger());
		model.train(cleanedData);
		model.evaluate(rapidminer);
		close();
	}

	public static void classify(File review) {
		double result = model.classify(rapidminer.cleanFile(review));
		frame.getLogger().logReview(review, result > 0);
		frame.getLogger().updateStats(result > 0);
	}

	public static void classifyMultiReviews(File selectedFile) {
		try {
			FileReader inStream = new FileReader(selectedFile);
			BufferedReader reader = new BufferedReader(inStream);
			String line = reader.readLine();
			String review = "";
			while (line != null) {
				if (line.equals("")) {
					double result = model
							.classify(rapidminer.cleanText(review));
					frame.getLogger().logReview(review, result > 0);
					frame.getLogger().updateStats(result > 0);
					review = "";
					line = reader.readLine();
					continue;
				}
				review += line + "\n";
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	private static void close(){
		
		fileParser.close();
	}
}
