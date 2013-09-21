/**
 * This class represents an SVM model.
 * @author Samy Shihata
 */

package models;

import gui.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.lang.ArrayUtils;
import org.apache.lucene.util.ArrayUtil;

import utils.RapidMinerInterface;

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Attributes;
import com.rapidminer.example.Example;
import com.rapidminer.example.set.SimpleExampleSet;
import com.rapidminer.operator.learner.functions.kernel.jmysvm.svm.SVMInterface;

import jnisvmlight.LabeledFeatureVector;
import jnisvmlight.SVMLightInterface;
import jnisvmlight.SVMLightModel;
import jnisvmlight.TrainingParameters;

public class SVMModel implements Runnable {

	private SVMLightInterface trainer;
	private SVMLightModel model;
	private Logger logger;

	public SVMModel(Logger logger) {
		trainer = new SVMLightInterface();
		SVMLightInterface.SORT_INPUT_VECTORS = true;
		this.logger = logger;
	}

	public void train(SimpleExampleSet cleanedData) {
		// Filling the feature vectors with data from the example set.
		System.out.print("Filling data vectors..");

		LabeledFeatureVector[] trainingData = new LabeledFeatureVector[cleanedData
				.size()];
		int currentVector = 0;

		for (Example example : cleanedData) {
			String label = example.getValueAsString(example.getAttributes()
					.getLabel());
			double value = label.equals("pos") ? 1.0 : -1.0;
			trainingData[currentVector] = createVector(example, value);
			trainingData[currentVector++].normalizeL2();

			System.out.print(".");
		}
		System.out.print("done. (" + currentVector + " vectores)\n");

		// Start Training the SVM model.
		System.out.print("\nTraining model..");
		TrainingParameters tp = new TrainingParameters();
		tp.getLearningParameters().verbosity = 1;
		model = trainer.trainModel(trainingData, tp);

		// Very expensive, crashes my laptop.
		// Thread writingThread = new Thread(this);
		// 5writingThread.start();

		System.out.print("done.\n");
	}

	public void evaluate(RapidMinerInterface rapidminer) {
		// Evaluate the current model
		System.out.print("Evaluating the model..");
		ArrayList<File> testingFiles = loadFiles();
		double percentage = 0;
		for (File file : testingFiles) {
			Example example = rapidminer.cleanFile(file);
			double label = file.getParentFile().getName().equals("pos") ? 1.0
					: -1.0;
			LabeledFeatureVector vector = createVector(example, label);
			double result = model.classify(vector);
			if ((result > 0 && label > 0) || (result < 0 && label < 0)) {
				percentage++;
			}
			logger.logReview(file, result > 0);
		}
		System.out.print("done. (" + testingFiles.size() + " examples)\n");
		System.out.println("   -- Accuracy ~= " + percentage
				/ testingFiles.size());
	}

	private LabeledFeatureVector createVector(Example example, double label) {
		Attributes attributes = example.getAttributes();
		double[] values = new double[attributes.size()];
		int[] dims = new int[attributes.size()];
		int i = 0;
		for (Attribute a : attributes) {
			values[i] = example.getValue(a);
			dims[i] = ++i;
		}
		return new LabeledFeatureVector(label, dims, values);
	}

	private ArrayList<File> loadFiles() {
		ArrayList<File> files = new ArrayList<>();
		File base = new File("resources/temp/testing set/pos");
		for (File file : base.listFiles()) {
			files.add(file);
		}
		base = new File("resources/temp/testing set/neg");
		for (File file : base.listFiles()) {
			files.add(file);
		}
		Collections.shuffle(files);
		return files;
	}

	public double classify(Example example) {
		LabeledFeatureVector vector = createVector(example, 0.0);
		return model.classify(vector);
	}

	// Background method to write the model to a file
	public void run() {
		model.writeModelToFile("SVM_model.dat");
	}
}
