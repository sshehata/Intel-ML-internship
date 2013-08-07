/**
 * Movie recommender app based on the SVM model.
 * @author Samy Shihata 
 */

package main;

import java.io.File;
import java.util.Scanner;

import models.SVMModel;
import utils.RapidMinerInterface;
import com.rapidminer.example.set.SimpleExampleSet;

public class SVMRecommender {
	private static SVMModel model;
	private static RapidMinerInterface rapidminer;
	private static int total;
	private static int pos;
	private static int neg;

	public static void main(String... args) {
		init();
		Scanner sc = new Scanner(System.in);
		while (true) {
			System.out.println("\nEnter a file path to classify(q to quit):");
			String path = sc.nextLine();
			if (path.equals("q")) {
				System.out.println("quitting..");
				break;
			}
			File file = new File(path);
			if (!file.isFile()) {
				System.out.println(path + ": No such file!");
				continue;
			}
			double label = model.classify(rapidminer.cleanFile(file));
			if (label > 0) {
				System.out.println("This review is positive");
				pos++;
			} else {
				System.out.println("This review is negative");
				neg++;
			}
			total++;
			System.out.println("Total: " + total);
			System.out.println("Positive: " + pos);
			System.out.println("Negative:" + neg);
			System.out.println("Movie score:" + (double) pos / total);
		}
		sc.close();
	}

	public static void init() {
		rapidminer = new RapidMinerInterface();
		SimpleExampleSet cleanedData = rapidminer.cleanTrainingData();
		model = new SVMModel(cleanedData.size());
		model.train(cleanedData);
		model.evaluate(rapidminer);
	}
}
