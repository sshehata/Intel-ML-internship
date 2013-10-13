/**
 * An interface to the RapidMiner API.
 * @author Samy Shihata
 */

package utils;

import java.io.File;
import java.io.IOException;
import gui.Logger;
import com.rapidminer.Process;
import com.rapidminer.RapidMiner;
import com.rapidminer.example.Example;
import com.rapidminer.example.set.SimpleExampleSet;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ProcessRootOperator;
import com.rapidminer.operator.nio.file.SimpleFileObject;
import com.rapidminer.operator.text.WordList;
import com.rapidminer.parameter.UndefinedParameterError;
import com.rapidminer.tools.XMLException;

public class RapidMinerInterface {

	private Process cleaningTrainingData;
	private WordList wordList;
	private Process cleaningFile;
	private Process cleaningText;

	public RapidMinerInterface(Logger logger, String trainingDataConfig,
			String classifyConfig, String classifyTextConfig) {
		RapidMiner.init();
		try {
			cleaningTrainingData = new Process(new File(trainingDataConfig));
			cleaningFile = new Process(new File(classifyConfig));
			System.out.println(cleaningTrainingData
				.getOperator("Process Documents from Files").getParameter("pos"));
			cleaningFile.getRootOperator().setParameter(
					ProcessRootOperator.PARAMETER_LOGVERBOSITY, "off");
			cleaningText = new Process(new File(classifyTextConfig));
			cleaningText.getRootOperator().setParameter(
					ProcessRootOperator.PARAMETER_LOGVERBOSITY, "off");
		} catch (IOException | XMLException e) {
			System.out.println(e);
		} catch (UndefinedParameterError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void findTrainingData() {
		File posFiles = new File("resources/training set/pos");
		File negFiles = new File("resources/training set/neg");
		Operator processDocuments = cleaningTrainingData
				.getOperator("Process Documents from Files");
		processDocuments.setParameter("pos", posFiles.getAbsolutePath());
		processDocuments.setParameter("neg", negFiles.getAbsolutePath());
	}

	public SimpleExampleSet cleanTrainingData() {
		try {
			IOContainer output = cleaningTrainingData.run();
			wordList = (WordList) output.getIOObjects()[1];
			return (SimpleExampleSet) output.getIOObjects()[0];
		} catch (OperatorException e) {
			System.out.println(e);
			return null;
		}
	}

	public Example cleanFile(File file) {
		try {
			IOContainer input = new IOContainer(new IOObject[] { wordList,
					new SimpleFileObject(file) });
			IOContainer output = cleaningFile.run(input);
			return ((SimpleExampleSet) output.getElementAt(0)).getExample(0);
		} catch (OperatorException e) {
			System.out.println(e);
			return null;
		}
	}

	public WordList getWordList() {
		return wordList;
	}

	public Example cleanText(String review) {
		try {
			IOContainer input = new IOContainer(new IOObject[] { wordList });
			cleaningText.getOperator("Create Document").setParameter("text",
					review);
			IOContainer output = cleaningText.run(input);
			return ((SimpleExampleSet) output.getElementAt(0)).getExample(0);
		} catch (OperatorException e) {
			System.out.println(e);
			return null;
		}
	}
}
