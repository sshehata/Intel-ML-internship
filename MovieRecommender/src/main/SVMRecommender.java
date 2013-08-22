/**
 * Movie recommender app based on the SVM model.
 * @author Samy Shihata 
 */

package main;

import gui.Frame;

import java.io.File;
import java.util.Scanner;

import models.SVMModel;
import utils.RapidMinerInterface;
import com.rapidminer.example.set.SimpleExampleSet;

public class SVMRecommender {
	private static SVMModel model;
	private static RapidMinerInterface rapidminer;
	private static Frame frame;

	public static void main(String... args) {
		init();
	}

	public static void init() {
		frame = new Frame();
		rapidminer = new RapidMinerInterface(frame.getLogger(),
				"cleaning_training_data.xml", "cleaning_file.xml", "cleaning_text.xml");
//		SimpleExampleSet cleanedData = rapidminer.cleanTrainingData();
//		model = new SVMModel(frame.getLogger());
//		model.train(cleanedData);
//		model.evaluate(rapidminer);
	}

	public static void classify(File review) {
		double result = model.classify(rapidminer.cleanFile(review));
		frame.getLogger().logReview(review, result > 0);
		frame.getLogger().updateStats(result > 0);
	}

	public static void classifyMultiReviews(File selectedFile) {
		rapidminer.cleanMultiReviewFile(selectedFile);
	}
}
