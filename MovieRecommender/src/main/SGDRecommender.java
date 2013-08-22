/**
 * Movie recommender app based on the SGD model.
 * @author Samy Shihata 
 */

package main;

import models.SGDModel;

import com.rapidminer.example.set.SimpleExampleSet;
import utils.RapidMinerInterface;

public class SGDRecommender {
	private static RapidMinerInterface rapidminer;
	private static SGDModel model;

	public static void main(String... args) {
		init();
	}

	public static void init() {
		rapidminer = new RapidMinerInterface("cleaning_training_data_SGD.xml",
				"cleaning_file_SGD.xml");
		SimpleExampleSet cleanedData = rapidminer.cleanTrainingData();
		model = new SGDModel(rapidminer.getWordList().size());
		model.train(cleanedData);
		model.evaluate(rapidminer);
	}
}
