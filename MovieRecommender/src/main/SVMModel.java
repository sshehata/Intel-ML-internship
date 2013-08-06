package main;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.Example;
import com.rapidminer.example.set.SimpleExampleSet;

import jnisvmlight.LabeledFeatureVector;
import jnisvmlight.SVMLightInterface;
import jnisvmlight.TrainingParameters;

public class SVMModel {

	private SVMLightInterface trainer;

	public SVMModel(int trainingSetSize) {
		trainer = new SVMLightInterface();
		SVMLightInterface.SORT_INPUT_VECTORS = true;
	}

	public void train(SimpleExampleSet cleanedData) {
		// Filling the feature vectors with data from the example set.
		System.out.print("Filling the vectors..");

		LabeledFeatureVector[] trainingData = new LabeledFeatureVector[cleanedData
				.size()];
		int currentVector = 0;

		for (Example example : cleanedData) {
			Attributes attributes = example.getAttributes();
			double value = example.getValueAsString(attributes.getLabel())
					.equals("pos") ? 1.0 : -1.0;
			double[] values = new double[attributes.size()];
			int[] dims = new int[attributes.size()];
			int i = 0;
			for (Attribute a : attributes) {
				values[i] = example.getValue(a);
				dims[i] = ++i;
			}
			trainingData[currentVector++] = new LabeledFeatureVector(value,
					dims, values);

			System.out.print(".");
		}
		System.out.print("DONE\n");

		// Start Training the SVM model.
		TrainingParameters tp = new TrainingParameters();
		tp.getLearningParameters().verbosity = 1;
		trainer.trainModel(trainingData, tp);
	}
}
