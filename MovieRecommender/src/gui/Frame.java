package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import utils.TweetCollector;
import main.SVMRecommender;

@SuppressWarnings("serial")
public class Frame extends JFrame {
	private Logger logger;

	public Frame() {
		super("Rotten Tomatoes");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(new Dimension(1000, 600));

		JPanel tempPanel = new JPanel();
		add(tempPanel, BorderLayout.CENTER);

		tempPanel.setLayout(new BorderLayout());
		ReviewPanel reviewPanel = new ReviewPanel();
		tempPanel.add(reviewPanel, BorderLayout.CENTER);

		LogPanel logPanel = new LogPanel();
		tempPanel.add(logPanel, BorderLayout.SOUTH);

		StatPanel statPanel = new StatPanel();
		add(statPanel, BorderLayout.EAST);

		initMenu();

		logger = new Logger(reviewPanel, logPanel, statPanel);

		repaint();
		setVisible(true);
	}

	private void initMenu() {
		add(new JMenuBar() {
			{
				add(new JMenu("File") {
					{
						add(new JMenu("Open") {
							private JFileChooser fc;

							{
								add(new JMenuItem("Single Review") {
									{
										fc = new JFileChooser();
										addActionListener(new ActionListener() {

											@Override
											public void actionPerformed(
													ActionEvent arg0) {
												if (fc.showOpenDialog(Frame.this) == JFileChooser.APPROVE_OPTION) {
													classifySingleReview(fc
															.getSelectedFile());
												}
											}
										});
									}
								});
								add(new JMenuItem("Multiple Review") {
									{
										addActionListener(new ActionListener() {

											@Override
											public void actionPerformed(
													ActionEvent arg0) {
												if (fc.showOpenDialog(Frame.this) == JFileChooser.APPROVE_OPTION) {
													classifyMultiReviews(fc
															.getSelectedFile());
												}
											}
										});
									}
								});

							}
						});
					}
				});

				add(new JMenuItem("Get Tweets From Twitter") {
					{
						addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent arg0) {
								String searchKeyword = JOptionPane
										.showInputDialog("Enter a Keyword to search for.");
								LoadingFrame loading = new LoadingFrame(SVMRecommender.frame);
								TweetCollector tweety = new TweetCollector(logger);
								logger.clearReviewPane();
								tweety.gatherTweets(searchKeyword);
								loading.dispose();
							}
						});
					}
				});

			}
		}, BorderLayout.NORTH);
	}

	public Logger getLogger() {
		return logger;
	}

	private void classifySingleReview(File file) {
		logger.clearReviewPane();

		String label = SVMRecommender.rapidminer.classify(SVMRecommender.parser
				.parseFile(file));
		int value = 0;
		if (label.equals("pos"))
			value = 1;
		else if (label.equals("neg"))
			value = -1;
		logger.updateStats(value);
	}

	private void classifyMultiReviews(File selectedFile) {
		try {
			logger.clearReviewPane();
			FileReader inStream = new FileReader(selectedFile);
			BufferedReader reader = new BufferedReader(inStream);
			String line = reader.readLine();
			String review = "";
			while (line != null) {
				if (line.equals("")) {
					
					String text = SVMRecommender.parser.parseText(review);
					if(text == null) continue;
					
					String label = SVMRecommender.rapidminer
							.classify(SVMRecommender.parser.parseText(review));
					int value = 0;
					if (label.equals("pos"))
						value = 1;
					else if (label.equals("neg"))
						value = -1;
					logger.updateStats(value);
					review = "";
					line = reader.readLine();
					continue;
				}
				review += line + "\n";
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
