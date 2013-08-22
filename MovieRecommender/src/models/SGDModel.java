/**
 * This class represents an SGD model.
 * @author Samy Shihata
 */

package models;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.mahout.classifier.evaluation.Auc;
import org.apache.mahout.classifier.sgd.L1;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.vectorizer.encoders.Dictionary;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;
import org.apache.mahout.vectorizer.encoders.StaticWordValueEncoder;

import utils.RapidMinerInterface;

import com.rapidminer.example.Example;
import com.rapidminer.example.set.SimpleExampleSet;
import com.rapidminer.tools.jep.function.expressions.Random;

public class SGDModel {

	private OnlineLogisticRegression logisticRegression;
	private Dictionary labels;
	private final int FEATURES;

	public SGDModel(int features) {
		FEATURES = features;
		logisticRegression = new OnlineLogisticRegression(2, FEATURES, new L1())
				.alpha(1).stepOffset(1000).decayExponent(0.9).lambda(3.0e-5)
				.learningRate(20);
		labels = new Dictionary();
	}

	public void train(SimpleExampleSet cleanedData) {
		// Filling the feature vectors with data from the example set.
		System.out.print("Training model..");

		for (Example example : cleanedData) {
			String label = example.getValueAsString(example.getAttributes()
					.getLabel());
			int actual = labels.intern(label);
			logisticRegression.train(actual, createVector(example));
			System.out.print(".");
		}
		System.out.println("done. (" + cleanedData.size() + " examples)\n");
		logisticRegression.close();
	}

	public void evaluate(RapidMinerInterface rapidminer) {
		// Evaluate the current model
		System.out.print("Evaluating the model..");
		ArrayList<File> testingFiles = loadFiles();
		Auc auc = new Auc();
		for (File file : testingFiles) {
			Example example = rapidminer.cleanFile(file);
			int actual = labels.intern(file.getParentFile().getName());
			RandomAccessSparseVector vector = createVector(example);
			Vector result = logisticRegression.classify(vector);
			auc.add(actual, result.get(0));
			System.out.print(".");
		}
		System.out.println("done.(" + testingFiles.size() + " examples)\n");
		System.out.println("  -- AUC ~= " + auc.auc());
		System.out.println("  -- Confusion" + auc.confusion());
		System.out.println("  -- Entropy" + auc.entropy());

	}

	private ArrayList<File> loadFiles() {
		ArrayList<File> files = new ArrayList<>();
		File base = new File("testing set/pos");
		for (File file : base.listFiles()) {
			files.add(file);
		}
		base = new File("testing set/neg");
		for (File file : base.listFiles()) {
			files.add(file);
		}
		return files;
	}

	private RandomAccessSparseVector createVector(Example example) {
		RandomAccessSparseVector vector = new RandomAccessSparseVector(FEATURES);
		FeatureVectorEncoder encoder = new StaticWordValueEncoder("body");
		StringTokenizer tokenizer = new StringTokenizer(
				(String) example.get("text"));
		while (tokenizer.hasMoreTokens()) {
			String word = tokenizer.nextToken();
			encoder.addToVector(word, 1.0, vector);
		}
		return vector;
	}
}
