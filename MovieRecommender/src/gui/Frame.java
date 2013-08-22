package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
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
													logger.clearReviewPane();
													SVMRecommender.classify(fc
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
													logger.clearReviewPane();
													SVMRecommender
															.classifyMultiReviews(fc
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
			}
		}, BorderLayout.NORTH);
	}

	public Logger getLogger() {
		return logger;
	}
}
