package gui;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.OutputStream;
import javax.swing.SwingUtilities;

public class Logger {
	private ReviewPanel reviewPanel;
	private LogPanel logPanel;
	private StatPanel statPanel;
	private OutputStream outStream;

	public Logger(ReviewPanel reviewPanel, LogPanel logPanel,
			StatPanel statPanel) {
		this.reviewPanel = reviewPanel;
		this.logPanel = logPanel;
		this.statPanel = statPanel;
		outStream = new LogOutputStream();
		System.setOut(new PrintStream(outStream));
		System.setErr(new PrintStream(outStream));
	}

	public void logReview(File review, int label) {
		reviewPanel.appendReview(review, label);
		String categ = "";
		if (label > 0)
			categ = "positive";
		else if (label < 0)
			categ = "negative";
		else
			categ = "neutral";
		logPanel.appendLine("Review from File " + review.getName()
				+ " classified as " + categ);
		logPanel.appendLine("\n");
	}

	public void logReview(String review, int label) {
		reviewPanel.appendReview(review, label);
		String categ = "";
		if (label > 0)
			categ = "positive";
		else if (label < 0)
			categ = "negative";
		else
			categ = "neutral";
		logPanel.appendLine("Review from Text classified as " + categ);
		logPanel.appendLine("\n");
	}

	public void updateStats(int label) {
		statPanel.updateState(label);
	}

	public void clearStats() {
		statPanel.clearState();
	}

	public void clearReviewPane() {
		reviewPanel.clear();
	}

	private class LogOutputStream extends OutputStream {
		private StringBuilder stringBuilder = new StringBuilder();

		@Override
		public void flush() {
		}

		@Override
		public void close() {
		}

		@Override
		public void write(int b) throws IOException {

			if (b == '\r')
				return;

			stringBuilder.append((char) b);
			final String text = stringBuilder.toString();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					logPanel.appendLine(text);
				}
			});
			stringBuilder.setLength(0);
			return;
		}
	}
}
