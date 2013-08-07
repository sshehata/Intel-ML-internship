package main;

import models.SVMModel;
import utils.RapidMinerInterface;
import com.rapidminer.example.set.SimpleExampleSet;

public class SVMRecommender {
	public static void main(String... args) {
		init();
	}

	public static void init() {
		RapidMinerInterface rapidminer = new RapidMinerInterface();
		SimpleExampleSet cleanedData = rapidminer.cleanTrainingData();
		SVMModel model = new SVMModel(cleanedData.size());
		model.train(cleanedData);
		model.evaluate(rapidminer);
	}
}
