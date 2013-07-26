package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.mahout.classifier.AbstractVectorClassifier;
import org.apache.mahout.classifier.sgd.L1;
import org.apache.mahout.classifier.sgd.OnlineLogisticRegression;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.Vector;
import org.apache.mahout.vectorizer.encoders.ConstantValueEncoder;
import org.apache.mahout.vectorizer.encoders.ContinuousValueEncoder;
import org.apache.mahout.vectorizer.encoders.Dictionary;
import org.apache.mahout.vectorizer.encoders.FeatureVectorEncoder;

public class TrainLogistic {
	
	private static OnlineLogisticRegression logisticRegression;
	private static Dictionary categories;

	public static void main(String... args) throws IOException {

		categories = new Dictionary();
		File trainingSet = new File("donut.csv");
		FileReader inStream = new FileReader(trainingSet);
		BufferedReader bReader = new BufferedReader(inStream);

		logisticRegression = new OnlineLogisticRegression(
				2, 20, new L1()).alpha(1 - 1.0e-3).lambda(1.0E-4)
				.learningRate(50.0);

		for (int i = 100; i > 0; i--) {
			String line = bReader.readLine();
			line = bReader.readLine();
			while (line != null) {
				String[] fields = line.split(",");
				Vector v = new RandomAccessSparseVector(20);
				FeatureVectorEncoder xEncoder = new ContinuousValueEncoder("x");
				FeatureVectorEncoder yEncoder = new ContinuousValueEncoder("y");
				FeatureVectorEncoder aEncoder = new ContinuousValueEncoder("a");
				FeatureVectorEncoder bEncoder = new ContinuousValueEncoder("b");
				FeatureVectorEncoder cEncoder = new ContinuousValueEncoder("c");
				FeatureVectorEncoder interceptEncoder = new ConstantValueEncoder(
						"intercept");

				xEncoder.addToVector(fields[0], v);
				yEncoder.addToVector(fields[1], v);
				aEncoder.addToVector(fields[9], v);
				bEncoder.addToVector(fields[10], v);
				cEncoder.addToVector(fields[11], v);
				interceptEncoder.addToVector((String) null, 1.0, v);
				int actual = categories.intern(fields[3]);
				logisticRegression.train(actual, v);

				line = bReader.readLine();
			}
			bReader.close();
			inStream = new FileReader(trainingSet);
			bReader = new BufferedReader(inStream);
		}

		logisticRegression.close();
		bReader.close();
		
		classify();
	}

	public static void classify() throws IOException {
		int total = 0;
		int correct = 0;
		int wrong = 0;
		
		File trainingSet = new File("donut-test.csv");
		FileReader inStream = new FileReader(trainingSet);
		BufferedReader bReader = new BufferedReader(inStream);
		
		String line = bReader.readLine();System.out.println(line);
		line = bReader.readLine();
		while (line != null) {
			String[] fields = line.split(",");
			Vector v = new RandomAccessSparseVector(20);
			FeatureVectorEncoder xEncoder = new ContinuousValueEncoder("x");
			FeatureVectorEncoder yEncoder = new ContinuousValueEncoder("y");
			FeatureVectorEncoder aEncoder = new ContinuousValueEncoder("a");
			FeatureVectorEncoder bEncoder = new ContinuousValueEncoder("b");
			FeatureVectorEncoder cEncoder = new ContinuousValueEncoder("c");
			FeatureVectorEncoder interceptEncoder = new ConstantValueEncoder(
					"intercept");

			xEncoder.addToVector(fields[0], v);
			yEncoder.addToVector(fields[1], v);
			aEncoder.addToVector(fields[8], v);
			bEncoder.addToVector(fields[9], v);
			cEncoder.addToVector(fields[7], v);
			interceptEncoder.addToVector((String)null,1, v);
			
			Vector result = logisticRegression.classify(v);
			int target = getTarget(result);
			if (target == categories.intern(fields[3])){
				correct++;
			} else {
				wrong++;
			}
			total++;			

			line = bReader.readLine();
		}
		System.out.println("The AUC is : " + (double)correct / (double)total);
		bReader.close();
	}
	
	public static int getTarget(Vector v){
		double zeroIndex = v.getElement(0).get();
		if (zeroIndex > (1 - zeroIndex))
			return 1;
		else
			return 0;
	}
}
