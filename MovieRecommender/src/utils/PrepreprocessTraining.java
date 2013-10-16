import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class PrepreprocessingTraining {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(
		// Insert the directory for your training data
				"C:\\Users\\Amr\\Desktop\\Intel ML\\twitter_training_set.csv")));
		// Insert the output directory for your positive reviews
		String positive_dir = "C:\\Users\\Amr\\Desktop\\Intel ML\\twitter_positive\\";
		// Insert the output directory for your negative reviews
		String negative_dir = "C:\\Users\\Amr\\Desktop\\Intel ML\\twitter_negative\\";
		BufferedWriter bufferedWriterPositive;
		BufferedWriter bufferedWriterNegative;
		String s;
		int i = 1;
		String[] arr;
		while ((s = br.readLine()) != null) {
			arr = s.split("\",\"");
			if (arr[0].substring(1).equals("0")) {
				bufferedWriterNegative = new BufferedWriter(new FileWriter(
						negative_dir + i + ".txt"));
				bufferedWriterNegative.write(arr[5].substring(0, arr[5].length() - 1));
				bufferedWriterNegative.flush();
			} else {
				bufferedWriterPositive = new BufferedWriter(new FileWriter(
						positive_dir + i + ".txt"));
				bufferedWriterPositive.write(arr[5].substring(0, arr[5].length() - 1));
				bufferedWriterPositive.flush();
			}
			System.out.println("Processing tweet number " + i);
			i++;
		}
	}

}
