/**
 * An interface to the RapidMiner API.
 * @author Samy Shihata
 */

package rapidminer;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import main.SVMRecommender;

import gui.LoadingFrame;
import gui.Logger;

import com.rapidminer.Process;
import com.rapidminer.RapidMiner;
import com.rapidminer.example.Example;
import com.rapidminer.example.set.SimpleExampleSet;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.Model;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.text.WordList;
import com.rapidminer.tools.XMLException;

public class RapidMinerInterface {
	private WordList wordList;
	private Model model;
	private Logger logger;

	private Process trainingProcess;
	private Process classifyingProcess;
	
	long start;
	
	static double neutralThreshold = 0.1;

	public RapidMinerInterface(Logger logger, String model) {
		RapidMiner.init();
		this.logger = logger;
		try {
			trainingProcess = new Process(new File(model + "train.xml"));
			classifyingProcess = new Process(new File(model + "classify.xml"));
		} catch (IOException | XMLException e) {
			System.out.println(e);
		}
	}

	public void train() {
		try {
			System.out.print("Begin training model...");
			start = new Date().getTime();
			LoadingFrame loading = new LoadingFrame(SVMRecommender.frame);
			IOContainer output = trainingProcess.run();
			System.out.println("done. (" + ((new Date().getTime() - start)/1000) + " seconds)");
			loading.dispose();
			model = (Model) output.getIOObjects()[0];
			wordList = (WordList) output.getIOObjects()[2];
			System.out.println("Evaluating Model.");
			System.out.println(output.getIOObjects()[1]);
		} catch (OperatorException e) {
			System.out.println(e);
		}
	}

	public String classify(String text) {
		IOContainer input = new IOContainer(new IOObject[] { model, wordList });
		classifyingProcess.getOperator("Create Document").setParameter("text",
				text);

		try {
			IOContainer output = classifyingProcess.run(input);
			Example result = ((SimpleExampleSet) output.getIOObjects()[0])
					.getExample(0);
			double pos = result.getValue(result.getAttributes().get(
					"confidence_pos"));
			double neg = result.getValue(result.getAttributes().get(
					"confidence_neg"));
			String label = "";
			if (Math.abs(pos - neg) > neutralThreshold)
				label = result.getValueAsString(result.getAttributes()
						.getPredictedLabel());
			else
				label = "neutral";
			int value = 0;
			if (label.equals("pos"))
				value = 1;
			else if (label.equals("neg"))
				value = -1;
			logger.logReview(text, value);
			return label;
		} catch (OperatorException e) {
			System.out.println(e);
			return null;
		}
	}
}
