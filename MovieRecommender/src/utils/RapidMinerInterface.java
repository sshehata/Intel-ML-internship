/**
 * An interface to the RapidMiner API.
 * @author Samy Shihata
 */

package utils;

import java.io.File;
import java.io.IOException;
import com.rapidminer.Process;
import com.rapidminer.RapidMiner;
import com.rapidminer.example.Example;
import com.rapidminer.example.set.SimpleExampleSet;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.IOObject;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ProcessRootOperator;
import com.rapidminer.operator.nio.file.SimpleFileObject;
import com.rapidminer.operator.text.WordList;
import com.rapidminer.tools.XMLException;

public class RapidMinerInterface {

	private Process cleaingingTrainingData;
	private WordList wordList;
	private Process cleaningFile;

	public RapidMinerInterface() {
		RapidMiner.init();
		try {
			cleaingingTrainingData = new Process(new File(
					"cleaning_training_data.xml"));
			cleaningFile = new Process(new File("cleaning_file.xml"));
			cleaningFile.getRootOperator().setParameter(
					ProcessRootOperator.PARAMETER_LOGVERBOSITY, "off");
		} catch (IOException | XMLException e) {
			System.out.println(e);
		}
	}

	public SimpleExampleSet cleanTrainingData() {
		try {
			IOContainer output = cleaingingTrainingData.run();
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
}
