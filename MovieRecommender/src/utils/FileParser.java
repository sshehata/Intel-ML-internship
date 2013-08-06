package utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class FileParser {
	private ArrayList<File> trainingSet;
	private ArrayList<File> testingSet;
	ArrayList<String> words;
	ArrayList<Integer> count;

	public void load() {
		trainingSet = new ArrayList<>();
		testingSet = new ArrayList<>();
		File base = new File("training set/pos");
		for (File file : base.listFiles()) {
			trainingSet.add(file);
		}
		base = new File("training set/neg");
		for (File file : base.listFiles()) {
			trainingSet.add(file);
		}
		base = new File("testing set/pos");
		for (File file : base.listFiles()) {
			testingSet.add(file);
		}
		base = new File("testing set/neg");
		for (File file : base.listFiles()) {
			testingSet.add(file);
		}

		Collections.shuffle(trainingSet);
		Collections.shuffle(testingSet);
	}

	public ArrayList<File> getTrainingSet() {
		return trainingSet;
	}

	public ArrayList<File> getTesterSet() {
		return testingSet;
	}
}
