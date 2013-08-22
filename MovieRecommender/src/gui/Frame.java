package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

import main.SVMRecommender;
import models.SVMModel;

public class Frame extends JFrame {
	private Logger logger;

	public Frame() {
		super("Rotten Tomatoes");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(new Dimension(800, 600));
		
		ReviewPanel reviewPanel = new ReviewPanel();
		add(reviewPanel, BorderLayout.CENTER);

		LogPanel logPanel = new LogPanel();
		add(logPanel, BorderLayout.SOUTH);

		StatPanel statPanel = new StatPanel();
		add(statPanel, BorderLayout.EAST);
		
		add(new JMenuBar(){
			{
				add(new JMenu("File"){
					{
						add(new JMenu("Open"){
							{
								add(new JMenuItem("Single Review"){
									{
										addActionListener(new ActionListener() {
											
											@Override
											public void actionPerformed(ActionEvent arg0) {
												JFileChooser fc = new JFileChooser();
												if(fc.showOpenDialog(Frame.this) == JFileChooser.APPROVE_OPTION){	
													logger.clearReviewPane();
													SVMRecommender.classify(fc.getSelectedFile());
												}
											}
										});
									}
								});
								add(new JMenuItem("Multiple Review"){
									{
										addActionListener(new ActionListener() {
											
											@Override
											public void actionPerformed(ActionEvent arg0) {
												JFileChooser fc = new JFileChooser();
												if(fc.showOpenDialog(Frame.this) == JFileChooser.APPROVE_OPTION){	
													logger.clearReviewPane();
													SVMRecommender.classifyMultiReviews(fc.getSelectedFile());
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

		logger = new Logger(reviewPanel, logPanel, statPanel);

		repaint();
		setVisible(true);
	}

	public Logger getLogger() {
		return logger;
	}
}
