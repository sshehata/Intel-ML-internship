package utils;

import java.io.File;
import java.io.IOException;

import com.rapidminer.Process;
import com.rapidminer.RapidMiner;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ProcessRootOperator;
import com.rapidminer.tools.LogService;
import com.rapidminer.tools.XMLException;

public class RapidMinerInterface {

	private Process cleaingingTrainingData;

	public RapidMinerInterface() {
		RapidMiner.init();
	}

	public IOContainer cleanTrainingData() {
		try {
			cleaingingTrainingData = new Process(new File(
					"cleaning_training_data.xml"));
			return cleaingingTrainingData.run();
		} catch (IOException e) {
			System.out.println(e);
			return null;
		} catch (XMLException e) {
			System.out.println(e);
			return null;
		} catch (OperatorException e) {
			System.out.println(e);
			return null;
		}

	}
}
