/**
 * Movie recommender app based on the SVM model.
 * @author Samy Shihata 
 */

package main;

import gui.Frame;
import rapidminer.Models;
import rapidminer.RapidMinerInterface;
import utils.Parser;

public class SVMRecommender {
	public static Frame frame;
	public static RapidMinerInterface rapidminer;
	public static Parser parser;

	public static void main(String... args) {
		init();
	}

	public static void init() {
		frame = new Frame();
		rapidminer = new RapidMinerInterface(frame.getLogger(),
				Models.SVM_MODEL);
		parser = new Parser();
		parser.parseTrainingData();
		rapidminer.train();
		close();
	}
	
	public static void close(){
		parser.close();
	}

}
