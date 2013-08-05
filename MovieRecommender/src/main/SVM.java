package main;

import java.util.HashSet;
import java.util.Iterator;

import jnisvmlight.LabeledFeatureVector;
import jnisvmlight.SVMLightInterface;
import jnisvmlight.TrainingParameters;

public class SVM {
	private final int TRAINING_SET_SIZE;

	private LabeledFeatureVector[] trainingData;
	private SVMLightInterface trainer;
	private int currentVector;

	public SVM(int trainingSetSize) {
		TRAINING_SET_SIZE = trainingSetSize;
		trainer = new SVMLightInterface();
		trainingData = new LabeledFeatureVector[TRAINING_SET_SIZE];
		SVMLightInterface.SORT_INPUT_VECTORS = true;
		currentVector = 0;
	}

	public void train(LabeledFeatureVector vector) {
		trainingData[currentVector++] = vector;
		if (currentVector == TRAINING_SET_SIZE) {
			TrainingParameters tp = new TrainingParameters();
			tp.getLearningParameters().verbosity = 1;
			trainer.trainModel(trainingData, tp);
		}
	}
}
