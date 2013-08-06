package main;

import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.IOObject;

import utils.RapidMinerInterface;
import jnisvmlight.SVMLightInterface;

public class SVMRecommender {
	public static void main(String...args){
		SVMLightInterface trainer = new SVMLightInterface();
		RapidMinerInterface rapidminer = new RapidMinerInterface();
		IOContainer output = rapidminer.cleanTrainingData();
		for (IOObject obj : output.asList()){
			System.out.println(obj);
		}
	}
}
