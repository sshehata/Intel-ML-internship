package main;

import com.rapidminer.example.set.SimpleExampleSet;
import utils.RapidMinerInterface;

public class SVMRecommender {
	public static void main(String... args) {
		RapidMinerInterface rapidminer = new RapidMinerInterface();
		SimpleExampleSet cleanedData = (SimpleExampleSet) rapidminer
				.cleanTrainingData().getIOObjects()[0];
		SVMModel model = new SVMModel(cleanedData.size());
		model.train(cleanedData);
	}
}
